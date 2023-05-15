import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UI extends JFrame {
    JTextField webName;
    JTextArea response;
    JPanel centreDisp;
    JButton  searchButton;


    public UI(){
        centreDisp = new JPanel();
        centreDisp.setLayout(null);

        webName = new JTextField("Enter your ");
        webName.setBounds(20,50,300,50);

        //Modify the response field;
        response = new JTextArea("Server response....");
        response.setEditable(false);
        response.setWrapStyleWord(true);
        response.setColumns(30);
        response.setBounds(20,120,500,400);

        centreDisp.setBounds(0,0,400,600);
        centreDisp.add(webName);
        centreDisp.add(response);



        searchButton= new JButton("Search");
        searchButton.setBackground(Color.CYAN);
        searchButton.setBounds(300,50,100,50);
        searchButton.addActionListener(actionEvent -> response.setText(" "));
        centreDisp.add(searchButton);

        add(centreDisp);
        setTitle("Bit Squatter");
        setLayout(null);
        setSize(450,600);
        setResizable(false);
        setLocationRelativeTo(null);


    }
    public static void main(String[]args){
        new  UI().setVisible(true);
    }

}
