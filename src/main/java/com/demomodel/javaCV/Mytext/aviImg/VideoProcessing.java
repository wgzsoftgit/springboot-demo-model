package com.demomodel.javaCV.Mytext.aviImg;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
 
/**
 * TODO：处理视频.（1.将视频提取成帧图片）
 *每一帧都是一张图片
 * @author ChenP
 */
public class VideoProcessing {
 
    //视频文件路径
    private static String videoPath = "D:/text/video";
 
    //视频帧图片存储路径
    public static String videoFramesPath = "D:/text/video/img/";
 
    /**
     * TODO 将视频文件帧处理并以“jpg”格式进行存储。
     * 依赖FrameToBufferedImage方法：将frame转换为bufferedImage对象
     *
     * @param videoFileName
     */
    public static void grabberVideoFramer(String videoFileName) {
        //Frame对象
        Frame frame = null;
        //标识
        int flag = 0;
        /*
            获取视频文件
         */
        FFmpegFrameGrabber fFmpegFrameGrabber = new FFmpegFrameGrabber(videoPath + "/" + videoFileName);
 
        try {
            fFmpegFrameGrabber.start();
            /*
            .getFrameRate()方法：获取视频文件信息,总帧数
             */
            int ftp = fFmpegFrameGrabber.getLengthInFrames();
//            System.out.println(fFmpegFrameGrabber.grabKeyFrame());
            System.out.println("时长 " + ftp / fFmpegFrameGrabber.getFrameRate() / 60);
 
            BufferedImage bImage = null;
            System.out.println("开始运行视频提取帧，耗时较长");
 
            while (flag <= ftp) {
                //文件绝对路径+名字
                String fileName = videoFramesPath + "/img_" + String.valueOf(flag) + ".jpg";
                //文件储存对象
                File outPut = new File(fileName);
                //获取帧
                frame = fFmpegFrameGrabber.grabImage();
//                System.out.println(frame);
                if (frame != null) {
                    ImageIO.write(FrameToBufferedImage(frame), "jpg", outPut);
                }
                flag++;
            }
            System.out.println("============运行结束============");
            fFmpegFrameGrabber.stop();
        } catch (IOException E) {
        }
    }
    public static BufferedImage FrameToBufferedImage(Frame frame) {
        //创建BufferedImage对象
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage bufferedImage = converter.getBufferedImage(frame);
        return bufferedImage;
    }
    /*
        测试.....
     */
    public static void main(String[] args) {
        String videoFileName = "chrome-download.mp4";
        grabberVideoFramer(videoFileName);
    }
    public static String getVideoPath() {
        return videoPath;
    }
    public static void setVideoPath(String videoPath) {
        VideoProcessing.videoPath = videoPath;
    }
}
