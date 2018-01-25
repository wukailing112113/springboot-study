package springboot.imagecut;

import lombok.Builder;
import lombok.Data;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;

/**
 * 把一张图片切割成几块，规则如下：
 * 把一张图片按照X*Y （几行几列）来均匀切割图片
 */
public class ImageCutter {
    public static List<BufferedImage> cutImage(Image img) throws IOException {
        BufferedImage image = ImageIO.read(img.getInputStream());
        int chunkWidth = image.getWidth() / img.getCols();
        int chunkHeight = image.getHeight() / img.getRows();
        return IntStream.range(0, img.getRows())
                .parallel()
                .mapToObj(x -> IntStream.range(0, img.getCols())
                        .parallel()
                        .mapToObj(y -> {
                            BufferedImage bufferedImage = new BufferedImage(chunkWidth, chunkHeight, image.getType());
                            Graphics2D gr = bufferedImage.createGraphics();
                            gr.drawImage(image, 0, 0,
                                    chunkWidth, chunkHeight,
                                    chunkWidth * y, chunkHeight * x,
                                    chunkWidth * y + chunkWidth, chunkHeight * x + chunkHeight,
                                    null);
                            gr.dispose();
                            return bufferedImage;
                        }))
                .flatMap(identity())
                .collect(toList());
    }

    @Data
    @Builder
    public static class Image {
        private final InputStream inputStream;
        private final int rows;
        private final int cols;

        public InputStream getInputStream() {
            return inputStream;
        }

        public int getRows() {
            return rows;
        }

        public int getCols() {
            return cols;
        }
    }

    public static void main(String[] args) throws Exception {
        File file = new File("D:\\spring-boot-work\\demo2\\springboot-study\\springboot-imagecut\\src\\main\\image\\atlas1.png");
        Image image = ImageCutter.Image.builder()
                .inputStream(new FileInputStream(file))
                .rows(2)
                .cols(2)
                .build();
        List<BufferedImage> bufferedImages = cutImage(image);

        String[] suffix = file.getName().split("\\.");
        for (int i = 0; i < bufferedImages.size(); i++) {
            ImageIO.write(bufferedImages.get(i), suffix[1], new File("D:\\spring-boot-work\\demo2\\springboot-study\\springboot-imagecut\\src\\main\\image/" + suffix[0] + i + "." + suffix[1]));
        }
        System.out.println("完成分割");
    }
}
