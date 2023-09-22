import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

public class ImageBasedLoginCaptcha {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 250;
    private static final int CAPTCHA_LENGTH = 5;
    private static final int NUM_CHOICES = 4;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Image-Based CAPTCHA");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout());
        JPanel captchaPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(captchaPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Generating the CAPTCHA text and choices
        final String captchaText = generateCaptchaText();
        String[] captchaChoices = generateCaptchaChoices(captchaText);

        // Displaying the CAPTCHA text
        JLabel captchaTextLabel = new JLabel("CAPTCHA Text:");
        topPanel.add(captchaTextLabel);

        JTextField captchaTextfield = new JTextField(captchaText);
        captchaTextfield.setEditable(false);
        topPanel.add(captchaTextfield);


        for (int i = 0; i < NUM_CHOICES; i++) {
            BufferedImage captchaImage = createCaptchaImage(captchaChoices[i]);
            JLabel captchaLabel = new JLabel(new ImageIcon(captchaImage));
            captchaLabel.setName(captchaChoices[i]);
            captchaLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JLabel selectedLabel = (JLabel) e.getSource();
                    String selectedChoice = selectedLabel.getName();
                    if (selectedChoice.equals(captchaText)) {
                        JOptionPane.showMessageDialog(frame, "Captcha Verified!");
                        frame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Captcha is incorrect. Please try again.");
                    }
                }
            });
            captchaPanel.add(captchaLabel);
        }

        // Verify button
        JButton verifyButton = new JButton("Verify");
        verifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Please click on the correct CAPTCHA image.");
            }
        });
        buttonPanel.add(verifyButton);

        frame.setVisible(true);
    }

    private static String generateCaptchaText() {
        // Generating a random alphanumeric string of CAPTCHA_LENGTH
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder captchaText = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < CAPTCHA_LENGTH; i++) {
            char randomChar = characters.charAt(random.nextInt(characters.length()));
            captchaText.append(randomChar);
        }
        return captchaText.toString();
    }

    private static String[] generateCaptchaChoices(String captchaText) {

        String[] choices = new String[NUM_CHOICES];
        Random random = new Random();
        int correctChoice = random.nextInt(NUM_CHOICES);
        for (int i = 0; i < NUM_CHOICES; i++) {
            if (i == correctChoice) {
                choices[i] = captchaText;
            } else {
                choices[i] = generateRandomString(CAPTCHA_LENGTH);
            }
        }
        return choices;
    }

    private static BufferedImage createCaptchaImage(String captchaText) {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // Set background color
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // Set random text color and font
        Random random = new Random();
        Color textColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        Font font = new Font(getRandomFontName(), getRandomFontStyle(), 30);
        g.setColor(textColor);
        g.setFont(font);

        // Draw the CAPTCHA text
        g.drawString(captchaText, 100, 100);

        
        for (int i = 0; i < 20; i++) {
            int x1 = random.nextInt(WIDTH);
            int y1 = random.nextInt(HEIGHT);
            int x2 = random.nextInt(WIDTH);
            int y2 = random.nextInt(HEIGHT);
            g.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            g.drawLine(x1, y1, x2, y2);
        }

        // Dispose of the graphics context
        g.dispose();

        return image;
    }

    private static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomString = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            char randomChar = characters.charAt(random.nextInt(characters.length()));
            randomString.append(randomChar);
        }
        return randomString.toString();
    }

    private static String getRandomFontName() {

        String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        Random random = new Random();
        return fontNames[random.nextInt(fontNames.length)];
    }

    private static int getRandomFontStyle() {
        // Generate a random font style (e.g., plain, bold, italic)
        Random random = new Random();
        int[] styles = {Font.PLAIN, Font.BOLD, Font.ITALIC};
        return styles[random.nextInt(styles.length)];
    }
}
