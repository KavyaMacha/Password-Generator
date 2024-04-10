import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Clipboard;
import java.io.*;
import java.util.*;




// render the GUI Components (frontend)
//this class will inherit from the JFrame class
public class PasswordGeneratorGUI extends JFrame
{
    private PasswordGenerator passwordGenerator; //which is responsible for the password gen and strength of the pass
    private JLabel passwordStrengthLabel; //display the strength of the generated password
    private JTextArea passwordLengthInputArea; //user input to specify the length of the password

    //private JTextArea passwordOutput;




    public PasswordGeneratorGUI() //constructor and it is responsible for initializing the GUI and setting the initial state
    {
        //render from and add a title
        super("Password Generator"); //adding the title /super is used to call the constructor of the superclass (Jframe)
        // and it sets the title to "Password Generator" to the GUI window

        //set the size of the GUI
        setSize(540,630);

        //prevent GUI from being able to resized
        setResizable(false);

        //we will set the layout to be null to have control over the position ans size of our components inn our app
        //which it means absolute positioning will be used to place components
        setLayout(null);

        //terminate the program when the GUI is closed(ends the process)
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //center the GUI to the screen
        //this sets the location of the GUI window to the center of the screen
        setLocationRelativeTo(null);

        //init password  generator
        //this is created to use its password generation and strength calculation
        passwordGenerator = new PasswordGenerator();


        // render GUI components
        //this method helps in adding the GUI components to the frame
        addGuiComponents();

        setVisible(true);

    }
    public void addGuiComponents(){
        //create title text

        JLabel titleLabel =new JLabel("Password Generator");

        // increase the font size and make it bold
        //styling
        titleLabel.setFont(new Font("Dialog", Font.BOLD,32));

        //center the text to the screen
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        //set x,y coordinates and  width/height values
        titleLabel.setBounds(0,10,540,39);

        //add to GUI
        add(titleLabel);

        //create result text area and to display the generator password
        JTextArea passwordOutput =new JTextArea();

        //prevent editing the text area
        //it is used  not to edit the generated password
        passwordOutput.setEditable(false);
        //styling
        passwordOutput.setFont(new Font("Dialog", Font.BOLD,32));

        //add scrollability in case output becomes too big
        //if the generated password too big we can scroll the password
        JScrollPane passwordOutputPane =new JScrollPane(passwordOutput);
        passwordOutputPane.setBounds(25,97,479,70);

        //create a block border around the text area

        passwordOutputPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(passwordOutputPane);

        //create password length label

        JLabel passwordLengthLabel=new JLabel("Password Length: ");
        passwordLengthLabel.setFont(new Font("Dialog", Font.PLAIN,32));
        passwordLengthLabel.setBounds(25,215,272,39);
        add(passwordLengthLabel);



        //create password length input
        JTextArea passwordLengthInputArea =new JTextArea();
        passwordLengthInputArea.setFont(new Font("Dialog", Font.PLAIN,32));
        passwordLengthInputArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        passwordLengthInputArea.setBounds(310,215,192,39);
        add(passwordLengthInputArea);



        //create toggle buttons
        //uppercase letter toggle
        JToggleButton uppercaseToggle =new JToggleButton("Uppercase");
        uppercaseToggle.setFont(new Font("Dialog", Font.PLAIN,26));
        uppercaseToggle.setBounds(25,302,225,56);
        add(uppercaseToggle);

        //lowercase letter toggle
        JToggleButton lowercaseToggle =new JToggleButton("Lowercase");
        lowercaseToggle.setFont(new Font("Dialog", Font.PLAIN,26));
        lowercaseToggle.setBounds(282,302,225,56);
        add(lowercaseToggle);

        //numbers toggle
        JToggleButton numbersToggle =new JToggleButton("Numbers");
        numbersToggle.setFont(new Font("Dialog", Font.PLAIN,26));
        numbersToggle.setBounds(25,373,225,56);
        add(numbersToggle);

        //symbols toggle
        JToggleButton symbolsToggle =new JToggleButton("Symbols");
        symbolsToggle.setFont(new Font("Dialog", Font.PLAIN,26));
        symbolsToggle.setBounds(282,373,225,56);
        add(symbolsToggle);

        // create the "Lowercase" checkbox






        //create generate button
        JButton generateButton =new JButton("Generate");
        generateButton.setFont(new Font("Dialog", Font.PLAIN,32));
        generateButton.setBounds(155,477,222,41);
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //validation: generate only when length>0 and one of the toggled is pressed
                if(passwordLengthInputArea.getText().length()<=0) return;
                boolean anyToggleSelected =lowercaseToggle.isSelected() ||
                        uppercaseToggle.isSelected()||
                        numbersToggle.isSelected()||
                        symbolsToggle.isSelected();

                //generate password
                int passwordLength = Integer.parseInt(passwordLengthInputArea.getText());
                if(anyToggleSelected)
                {
                    String generatePassword = passwordGenerator.generatePassword(passwordLength,
                            uppercaseToggle.isSelected(),
                            lowercaseToggle.isSelected(),
                            numbersToggle.isSelected(),
                            symbolsToggle.isSelected());
                    passwordOutput.setText(generatePassword);


                    // Calculate and display password strength
                    PasswordStrength strength = passwordGenerator.calculatePasswordStrength(generatePassword);
                    String strengthMessage;
                    switch (strength) {
                        case WEAK:
                            strengthMessage = "Password Strength: Weak";
                            break;
                        case MEDIUM:
                            strengthMessage = "Password Strength: Medium";
                            break;
                        case STRONG:
                            strengthMessage = "Password Strength: Strong";
                            break;
                        default:
                            strengthMessage = "Password Strength: Unknown";
                            break;
                    }
                    passwordStrengthLabel.setText(strengthMessage);

                }
            }
        });
        add(generateButton);

        passwordStrengthLabel = new JLabel();
        passwordStrengthLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        passwordStrengthLabel.setBounds(25, 520, 479, 30);
        passwordStrengthLabel.setHorizontalAlignment(SwingConstants.CENTER);
        passwordStrengthLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
        add(passwordStrengthLabel);


        JButton copyButton = new JButton("Copy to Clipboard");
        copyButton.setFont(new Font("Dialog", Font.PLAIN, 20));
        copyButton.setBounds(25, 560, 220, 30);
        copyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String password = passwordOutput.getText();
                if (!password.isEmpty()) {
                    StringSelection selection = new StringSelection(password);
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(selection, null);
                    JOptionPane.showMessageDialog(null, "Password copied to clipboard!");
                }
            }
        });
        add(copyButton);


        JButton saveButton = new JButton("Save Password");
        saveButton.setFont(new Font("Dialog", Font.PLAIN, 18));
        saveButton.setBounds(260, 560, 230, 26);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String generatedPassword = passwordOutput.getText();
                if (!generatedPassword.isEmpty()) {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Save Password");
                    int userSelection = fileChooser.showSaveDialog(PasswordGeneratorGUI.this);
                    if (userSelection == JFileChooser.APPROVE_OPTION) {
                        File fileToSave = fileChooser.getSelectedFile();
                        FileUtil.saveStringToFile(generatedPassword, fileToSave);
                        JOptionPane.showMessageDialog(PasswordGeneratorGUI.this,
                                "Password saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(PasswordGeneratorGUI.this,
                            "No password generated yet!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(saveButton);

        // Create the custom character set text field


    }

    static class FileUtil {
        public static void saveStringToFile(String content, File file) {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
                writer.write(content);
                writer.newLine();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}