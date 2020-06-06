package dynamicWallpaper;

import java.util.*;

import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

public class dynWallpaper extends JFrame implements ActionListener{
    public boolean run = false;
    public static File log = new File("log");
    public static File config = new File("config.properties");
    private static final long serialVersionUID = 1L;
    final String newline = "\n";
    private JPanel paper1; JFileChooser fc;
    private JButton openButton1, openButton2, openButton3, openButton4, save, wallpaperOne, wallpaperTwo, wallpaperThree, wallpaperFour;
    File wallpaper1, wallpaper2, wallpaper3, wallpaper4;
    public dynWallpaper() {
        //top = new JPanel();
        fc = new JFileChooser();
        openButton1 = new JButton("Choose a picture for 8PM to 6AM...");
        openButton1.addActionListener(this);
        openButton2 = new JButton("Choose a picture for 6AM to 12PM");
        openButton2.addActionListener(this);
        openButton3 = new JButton("Choose a picture for 12PM to 5PM");
        openButton3.addActionListener(this);
        openButton4 = new JButton("Choose a picture for 5PM to 8PM");
        openButton4.addActionListener(this);
        save = new JButton("Save");
        save.addActionListener(this);
        paper1 = new JPanel(new GridLayout(5, 1));
        paper1.add(openButton1);
        paper1.add(openButton2);
        paper1.add(openButton3);
        paper1.add(openButton4);
        paper1.add(save);
        add(paper1);
        setSize(300, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    public static void wallpaper() throws Exception {
        int savedHours = 0;
        while (true) {
            try (InputStream input = new FileInputStream("config.properties")) {
                BufferedWriter writer = new BufferedWriter(new FileWriter("log", true));
                Properties prop = new Properties();
                prop.load(input);
                long millis = System.currentTimeMillis();
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(millis);
                int hours = c.get(Calendar.HOUR);
                int AMorPM = c.get(Calendar.AM_PM);
                System.out.println(hours + "" + savedHours);
                if(savedHours == hours) {} else {
                    if(AMorPM == 0) {
                        if(hours < 6 || hours == 12) {
                            setWallpaper(prop.getProperty("wallpaperNight"));
                            savedHours = hours;
                            writer.append("\nSet wallpaper to " + prop.getProperty("wallpaperNight"));
                        } else if(hours >= 6 && hours < 12 ) { 
                            //set wallpaper to sunrise
                            setWallpaper(prop.getProperty("wallpaperSunrise"));
                            savedHours = hours;
                            writer.append("\nSet wallpaper to " + prop.getProperty("wallpaperSunrise"));

                        }
                    } else { 
                        if((hours >= 1 && hours < 5) || hours == 12) {
                            //set wallpaper to noon
                            setWallpaper(prop.getProperty("wallpaperNoon"));
                            savedHours = hours;
                            writer.append("\nSet wallpaper to " + prop.getProperty("wallpapeNoon"));
                        } else if(hours >= 5 && hours < 8) {
                            //late afternoon
                            setWallpaper(prop.getProperty("wallpaperEvening"));
                            savedHours = hours;
                            writer.append("\nSet wallpaper to " + prop.getProperty("wallpaperEvening"));


                        } else if(hours >= 8 && hours < 12) { 
                            //night
                            setWallpaper(prop.getProperty("wallpaperNight"));
                            savedHours = hours;
                            writer.append("\nSet wallpaper to " + prop.getProperty("wallpaperNight"));


                        }
                    }
                }
                Thread.sleep(5 * 1000);
                writer.append("\nChecking...(" + System.currentTimeMillis() + ")");
                System.out.println("Checking...");
                writer.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == openButton1) {
            int returnVal = fc.showOpenDialog(dynWallpaper.this);
 
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                wallpaper1 = fc.getSelectedFile();
                //This is where a real application would open the file.
            } else {
            }
            openButton1.setText(wallpaper1.getName());
 
        //Handle save button action.
        } else if (e.getSource() == openButton2) {
            int returnVal = fc.showOpenDialog(dynWallpaper.this);
 
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                wallpaper2 = fc.getSelectedFile();
                //This is where a real application would open the file.
            } else {
            }
            openButton2.setText(wallpaper2.getName());
        } else if(e.getSource() == openButton3) {
            int returnVal = fc.showOpenDialog(dynWallpaper.this);
 
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                wallpaper3 = fc.getSelectedFile();
                //This is where a real application would open the file.
            } else {
            }
            openButton3.setText(wallpaper3.getName());
        } else if(e.getSource() == openButton4) {
            int returnVal = fc.showOpenDialog(dynWallpaper.this);
 
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                wallpaper4 = fc.getSelectedFile();
                //This is where a real application would open the file.
            } else {
            }
            openButton4.setText(wallpaper4.getName());
        } else if(e.getSource() == save) {
            try {
                Properties storage = new Properties();
                storage.setProperty("wallpaperMorning", wallpaper1.getPath());
                storage.setProperty("wallpaperSunrise", wallpaper2.getPath());
                storage.setProperty("wallpaperNoon", wallpaper3.getPath());
                storage.setProperty("wallpaperEvening", wallpaper4.getPath());
                storage.setProperty("wallpaperNight", wallpaper4.getPath());
                storage.setProperty("run", "true");
                FileOutputStream output = new FileOutputStream(config);
                storage.store(output, "Wallpaper Filepath Storage");
            } catch (Exception io) {
                io.printStackTrace();
            }
        }
    }
    
    public static void setWallpaper(String filepath) throws Exception {
        //System.out.println(filepath);
        String as[] = {
            "osascript", 
            "-e", "tell application \"Finder\"", 
            "-e", "set desktop picture to POSIX file \"" + filepath + "\"",
            "-e", "end tell"
        };
        Runtime runtime = Runtime.getRuntime();
        Process process;
        process = runtime.exec(as);
    }
    public static void main(String[] arg) throws Exception {
        try (InputStream input = new FileInputStream("config.properties")) {
            Properties prop = new Properties();
            prop.load(input);   
            String r = prop.getProperty("run");
            if(r != null && r.equals("true")) {
                System.out.println("Running");
                wallpaper();
            } else {
                dynWallpaper dyn = new dynWallpaper();
                System.out.println("Storing Data");
            }
        }
    }
}