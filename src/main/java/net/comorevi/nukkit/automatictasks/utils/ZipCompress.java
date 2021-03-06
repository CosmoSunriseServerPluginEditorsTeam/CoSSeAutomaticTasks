package net.comorevi.nukkit.automatictasks.utils;

import net.comorevi.nukkit.automatictasks.AutomaticTasks;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipCompress {

    AutomaticTasks automaticTasks;

    public ZipCompress (AutomaticTasks plugin) {
        this.automaticTasks = plugin;
    }

    /**
     * 指定されたディレクトリ内のファイルを ZIP アーカイブし、指定されたパスに作成します。
     * デフォルト文字コードは Shift_JIS ですので、日本語ファイル名も対応できます。
     *
     * @param filePath 圧縮後の出力ファイル名をフルパスで指定 ( 例: C:/sample.zip )
     * @param directory 圧縮するディレクトリ ( 例; C:/sample )
     * @return 処理結果 true:圧縮成功 false:圧縮失敗
     */
    public static boolean compressDirectory( String filePath, String directory ) {
        File baseFile = new File(filePath);
        File file = new File(directory);
        ZipOutputStream outZip = null;
        try {
            // ZIPファイル出力オブジェクト作成
            outZip = new ZipOutputStream(new FileOutputStream(baseFile));
            archive(outZip, baseFile, file);
        } catch ( Exception e ) {
            // ZIP圧縮失敗
            return false;
        } finally {
            // ZIPエントリクローズ
            if ( outZip != null ) {
                try { outZip.closeEntry(); } catch (Exception e) {}
                try { outZip.flush(); } catch (Exception e) {}
                try { outZip.close(); } catch (Exception e) {}
            }
        }
        return true;
    }

    /**
     * ディレクトリ圧縮のための再帰処理
     *
     * @param outZip ZipOutputStream
     * @param baseFile File 保存先ファイル
     * @param targetFile 圧縮したいファイル
     */
    private static void archive(ZipOutputStream outZip, File baseFile, File targetFile) {
        if ( targetFile.isDirectory() ) {
        File[] files = targetFile.listFiles();

        //add start 空フォルダも圧縮するように
        if(files.length == 0){
            archive(outZip, baseFile, targetFile, targetFile.getAbsolutePath().replace(baseFile.getParent(), "").substring(1) + File.separator, "Shift_JIS");
        }
        //add end
            for (File f : files) {
                if ( f.isDirectory() ) {
                    archive(outZip, baseFile, f);
                } else {
                    if ( !f.getAbsoluteFile().equals(baseFile)) {
                        // 圧縮処理
                        archive(outZip, baseFile, f, f.getAbsolutePath().replace(baseFile.getParent(), "").substring(1), "Shift_JIS");
                    }
                }
            }
        }
    }

    /**
     * 圧縮処理
     *
     * @param outZip ZipOutputStream
     * @param baseFile File 保存先ファイル
     * @param targetFile File 圧縮したいファイル
     * @parma entryName 保存ファイル名
     * @param enc 文字コード
     */
    private static boolean archive(ZipOutputStream outZip, File baseFile, File targetFile, String entryName, String enc) {
        // 圧縮レベル設定
        outZip.setLevel(5);

        // 文字コードを指定
        outZip.setComment(enc);
        try {

            // ZIPエントリ作成
            outZip.putNextEntry(new ZipEntry(entryName));

            // 圧縮ファイル読み込みストリーム取得
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(targetFile));

            // 圧縮ファイルをZIPファイルに出力
            int readSize = 0;
            byte buffer[] = new byte[1024]; // 読み込みバッファ
            while ((readSize = in.read(buffer, 0, buffer.length)) != -1) {
                outZip.write(buffer, 0, readSize);
            }
            // クローズ処理
            in.close();
            // ZIPエントリクローズ
            outZip.closeEntry();
        } catch ( Exception e ) {
            // ZIP圧縮失敗
            return false;
        }
        return true;
    }

}
