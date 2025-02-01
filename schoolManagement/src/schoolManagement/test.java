package schoolManagement;
import java.awt.*;
import javax.swing.*;

public class test {

    public static void main(String[] args) {
        Frame frame = new Frame("Label with Border Example");

        // Panel을 사용하여 테두리를 추가
        Panel panel = new Panel(new FlowLayout());
        panel.setBackground(Color.black); // 테두리 색상 설정
        panel.add(new Label("Hello, AWT!"));
        
        frame.add(panel);
        frame.setSize(300, 100);
        frame.setVisible(true);
        
        String str="12345";
        System.out.println(str.substring(0,1));
    }
}
