package ohtu.data_access;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import ohtu.domain.User;

public class FileUserDao implements UserDao {

    private Scanner lukija;
    private File tiedosto;
    private FileWriter kirjoittaja;

    public FileUserDao(String tiedostonNimi) {
        this.tiedosto = new File(tiedostonNimi);
        try {
            this.lukija = new Scanner(this.tiedosto);
            this.kirjoittaja = new FileWriter(this.tiedosto, true);
        } catch (IOException ex) {
            Logger.getLogger(FileUserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public List<User> listAll() {   
        alustaLukija();
        List<User> lista = new ArrayList<User>();
        while (lukija.hasNextLine()) {
            String rivi = lukija.nextLine();
            String[] nimiJaSalasana = rivi.split(":");
            lista.add(new User(nimiJaSalasana[0], nimiJaSalasana[1]));
        }
        return lista;
    }
    
    private void alustaLukija() {
        try {
            lukija = new Scanner(this.tiedosto);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileUserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public User findByName(String name) {
        while (lukija.hasNextLine()) {
            String rivi = lukija.nextLine();
            String[] nimiJaSalasana = rivi.split(":");
            if (nimiJaSalasana[0].equals(name)) {
                return new User(nimiJaSalasana[0], nimiJaSalasana[1]);
            }
        }
        return null;
    }

    @Override
    public void add(User user) {
        try {
            this.kirjoittaja = new FileWriter(tiedosto, true);
            kirjoittaja.write(user.getUsername() + ":" + user.getPassword() + "\n");
            kirjoittaja.close();
        } catch (IOException ex) {
            Logger.getLogger(FileUserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
