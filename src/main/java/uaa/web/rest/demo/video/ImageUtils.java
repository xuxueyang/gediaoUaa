package uaa.web.rest.demo.video;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;

public final class ImageUtils {

    //缩略图默认长宽限制
    private static final int THUMBNAIL_DEFAULT_LIMIT = 400;

    private ImageUtils() { }

    /**
     * 根据长宽值缩放
     * @author sunk
     */
    public static BufferedImage scaleByWh(BufferedImage source, int width, int height) {
        return getBufferedImageLocal(source.getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }
    public static void addVideoPlayMark(BufferedImage image){
        // 1、得到画笔对象
        Graphics2D g = image.createGraphics();
        int width = image.getWidth();
        int height = image.getHeight();
        int num = 5;
        int r = width > height ? height/num:width/num;
        float c = 0.1f;
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setColor(new Color(c,c,c,0.8f));
        g.fillOval(width/2-r/2, height/2-r/2, r, r);
        int rightx = width/2+r/6+2;
        int righty = height/2;
        int lefttopx = width/2-r/6+2;
        int leftbottomx = lefttopx;
        int i = (int) ((0.1 + r) / 3 / 1.7);
        int lefttopy = height/2 + i;
        int leftbottomy = height/2 - i;


        int px[]={rightx,lefttopx,leftbottomx};//首末点相重,才能画多边形
        int py[]={righty,lefttopy,leftbottomy};
        g.setColor(Color.white);
        g.fillPolygon(px,py,3);

//        // 2、设置对线段的锯齿状边缘处理
//        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//        g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg.getHeight(null), Image.SCALE_SMOOTH), 0,
//            0, null);
//        // 3、设置水印旋转
//        if (0 != degree) {
//            g.rotate(Math.toRadians(degree), (double) buffImg.getWidth() / 2, (double) buffImg.getHeight() / 2);
//        }
//
//        // 4、水印图片的路径 水印图片一般为gif或者png的，这样可设置透明度
//        ImageIcon imgIcon = new ImageIcon(waterImgPath);
//
//        // 5、得到Image对象。
//        Image img = imgIcon.getImage();
//
//        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
//
//        // 6、水印图片的位置
//        for (int height = interval + imgIcon.getIconHeight(); height < buffImg.getHeight(); height = height
//            + interval + imgIcon.getIconHeight()) {
//            for (int weight = interval + imgIcon.getIconWidth(); weight < buffImg.getWidth(); weight = weight
//                + interval + imgIcon.getIconWidth()) {
//                g.drawImage(img, weight - imgIcon.getIconWidth(), height - imgIcon.getIconHeight(), null);
//            }
//        }

        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        // 7、释放资源
        g.dispose();

        // 8、生成图片
    }
    /**
     * 根据长宽限制缩放
     * <p> 限制较大的一边
     * @author sunk
     */
    public static BufferedImage scaleByWhLimit(BufferedImage source, int limit) {
        int scaleW = -1;
        int scaleH = -1;

        if (source.getWidth() > source.getHeight()) {
            scaleW = limit;
        } else {
            scaleH = limit;
        }
        return scaleByWh(source, scaleW, scaleH);
    }

    /**
     * 根据比例缩放
     * @author sunk
     */
    public static BufferedImage scaleByRatio(BufferedImage source, double ratio) {

        int w = (int) (source.getWidth() * ratio);
        int h = (int) (source.getHeight() * ratio);
        return scaleByWh(source, w, h);
    }

    /**
     * 将 Image 转为 BufferedImage
     * @author sunk
     */
    public static BufferedImage getBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        BufferedImage bufImage = new BufferedImage(img.getWidth(null),
            img.getHeight(null), BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = bufImage.createGraphics();
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();

        return bufImage;
    }

    /**
     * 将 Image 转为 BufferedImage
     * @author sunk
     */
    public static BufferedImage getBufferedImageLocal(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();//TODO 需要注意硬件支持
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        BufferedImage bufImage = gc.createCompatibleImage(img.getWidth(null),
            img.getHeight(null));

        Graphics2D g2d = bufImage.createGraphics();
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();

        return bufImage;
    }

    /**
     * 获取缩略图
     * @author sunk
     */
    public static ByteArrayOutputStream getThumbnail(BufferedImage sourceBi,
                                                     int limit) throws IOException {

        //缩放
        BufferedImage scaledBi = scaleByWhLimit(sourceBi, limit);

        //压缩
        ByteArrayOutputStream compressedOs = new ByteArrayOutputStream();
        ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();
        jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        jpgWriteParam.setCompressionQuality(0.5f);

        jpgWriter.setOutput(ImageIO.createImageOutputStream(compressedOs));
        jpgWriter.write(null, new IIOImage(scaledBi, null, null), jpgWriteParam);

        return compressedOs;
    }

    /**
     * 获取缩略图
     * @author sunk
     */
    public static ByteArrayOutputStream getThumbnail(BufferedImage sourceBi)
        throws IOException {
        return getThumbnail(sourceBi, THUMBNAIL_DEFAULT_LIMIT);
    }

    /**
     * 获取缩略图
     * @author sunk
     */
    public static ByteArrayOutputStream getThumbnail(File sourceFile, int limit)
        throws IOException {

        return getThumbnail(ImageIO.read(sourceFile), limit);
    }

    /**
     * 获取缩略图
     * @author sunk
     */
    public static ByteArrayOutputStream getThumbnail(File source)
        throws IOException {

        return getThumbnail(ImageIO.read(source));
    }
}
