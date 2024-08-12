import java.awt.*;
import java.awt.Rectangle;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

public class kadai extends JFrame implements KeyListener {
    public kadai() {
        // JFrame の設定
        setTitle("Press Enter to Capture Screen");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addKeyListener(this);
        setVisible(true);
    }

    public static void main(String[] args) {
        new kadai();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Enterキーが押されたかをチェック
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                // 1. スクリーン全体をキャプチャ
                Robot robot = new Robot();
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                Rectangle screenRect = new Rectangle(0, 0, screenSize.width, screenSize.height);
                BufferedImage screenCapture = robot.createScreenCapture(screenRect);

                // 2. グレースケールに変換
                BufferedImage grayImage = new BufferedImage(screenCapture.getWidth(), screenCapture.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
                Graphics g = grayImage.getGraphics();
                g.drawImage(screenCapture, 0, 0, null);
                g.dispose();

                // 3. ラプラシアンフィルタのカーネル
                int[][] laplacianKernel = {
                    { 0, -1,  0 },
                    { -1,  4, -1 },
                    { 0, -1,  0 }
                };

                // 4. フィルタの適用
                BufferedImage outputImage = new BufferedImage(grayImage.getWidth(), grayImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

                for (int y = 1; y < grayImage.getHeight() - 1; y++) {
                    for (int x = 1; x < grayImage.getWidth() - 1; x++) {
                        int newValue = 0;
                        for (int ky = -1; ky <= 1; ky++) {
                            for (int kx = -1; kx <= 1; kx++) {
                                int pixelValue = grayImage.getRaster().getSample(x + kx, y + ky, 0);
                                newValue += pixelValue * laplacianKernel[ky + 1][kx + 1];
                            }
                        }

                        // 新しいピクセル値を0~255にする
                        newValue = Math.min(Math.max(newValue, 0), 255);

                        // 出力画像にセット
                        outputImage.getRaster().setSample(x, y, 0, newValue);
                    }
                }

                // 5. 結果をファイルに保存
                ImageIO.write(outputImage, "png", new File("laplacian_result.png"));
                System.out.println("Enterキーが押されたので、スクリーンキャプチャを実行しました。");

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
