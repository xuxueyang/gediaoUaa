package uaa.web.rest.demo.video;

import java.awt.image.BufferedImage;
import java.io.*;

import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

public final class VideoUtils {
    private VideoUtils() { }
    private static final int THUMB_FRAME = 5;

    public static class FileSelect {
        public static File selectFilesAndDir() {
            JFileChooser jfc = new JFileChooser();
            //设置当前路径为桌面路径,否则将我的文档作为默认路径
            FileSystemView fsv = FileSystemView.getFileSystemView();
            jfc.setCurrentDirectory(fsv.getHomeDirectory());
            //JFileChooser.FILES_AND_DIRECTORIES 选择路径和文件
            jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            //弹出的提示框的标题
            jfc.showDialog(new JLabel(), "确定");
            //用户选择的路径或文件
            File file = jfc.getSelectedFile();
            return file;
        }
    }
//    public static  void main(String[] args){
////        File file = FileSelect.selectFilesAndDir();
//        File file = new File("C:\\Users\\Administrator\\Desktop\\rrrr.mp4");
//        try {
////            BufferedImage frame = getFrame(file, 10);
////            File outputfile  = new File("D:\\gediaosave.jpg");
////            ImageIO.write(frame,"jpg",outputfile);
//
////            ByteArrayOutputStream thumbnail = getThumbnail(file);
////            InputStream is = new ByteArrayInputStream(thumbnail.toByteArray());
////            BufferedImage image = ImageIO.read(is);
//            BufferedImage image = VideoUtils.getFrame(file, 10);
//            BufferedImage resizeImage = ImageUtils.getResizeImage(image);
//
//            ImageUtils.addVideoPlayMark(resizeImage);
//            File outputfile  = new File("C:\\Users\\Administrator\\Desktop\\save.jpg");
//
////            ImageUtils.addVideoPlayMark(image);
//
//            ImageIO.write(resizeImage,"jpg",outputfile);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JCodecException e) {
//            e.printStackTrace();
//        }
//    }



    /**
     * 获取视频指定帧
     * @author sunk
     */
    public static BufferedImage getFrame (File file, int frameNumber)
        throws IOException, JCodecException {

        Picture picture = FrameGrab.getFrameFromFile(file, frameNumber);
        return AWTUtil.toBufferedImage(picture);
    }
//
//    /**
//     * 获取缩略图
//     * @author sunk
//     */
//    public static ByteArrayOutputStream getThumbnail(File file,
//                                                     int limit) throws IOException, JCodecException {
//
//        BufferedImage frameBi = getFrame(file, THUMB_FRAME);
//        return ImageUtils.getThumbnail(frameBi, limit);
//    }
//
//    /**
//     * 获取缩略图
//     * @author sunk
//     */
//    public static ByteArrayOutputStream getThumbnail(File file)
//        throws IOException, JCodecException {
//
//        BufferedImage frameBi = getFrame(file, THUMB_FRAME);
//        return ImageUtils.getThumbnail(frameBi);
//    }
}
