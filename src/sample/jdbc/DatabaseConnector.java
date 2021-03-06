package sample.jdbc;

import sample.entity.*;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class DatabaseConnector{

    public static final String DB_NAME = "production.db";

    public static final String TABLE_BILETY = "Bilety";
    public static final String COLUMN_BILETY_ID_BILETU = "id_biletu";
    public static final String COLUMN_BILETY_ID_SEANSU = "id_seansu";
    public static final String COLUMN_BILETY_ID_FILMU = "id_filmu";
    public static final String COLUMN_BILETY_ID_SALI = "id_sali";
    public static final String COLUMN_BILETY_ID_MIEJSCA = "id_miejsca";
    public static final String COLUMN_BILETY_ID_TRANSAKCJI = "id_transakcji";
    public static final String COLUMN_BILETY_TYP_BILETU = "typ_biletu";
    public static final int INDEX_BILETY_ID_BILETU = 1;
    public static final int INDEX_BILETY_ID_SEANSU = 2;
    public static final int INDEX_BILETY_ID_FILMU = 3;
    public static final int INDEX_BILETY_ID_SALI = 4;
    public static final int INDEX_BILETY_ID_MIEJSCA = 5;
    public static final int INDEX_BILETY_ID_TRANSAKCJI = 6;
    public static final int INDEX_BILETY_TYP_BILETU = 7;

    public static final String TABLE_FILMY = "Filmy";
    public static final String COLUMN_FILMY_ID_FILMU = "id_filmu";
    public static final String COLUMN_FILMY_CZAS_TRWANIA = "czas_trwania";
    public static final String COLUMN_FILMY_OPIS = "opis";
    public static final String COLUMN_FILMY_TYTUL = "tytul";
    private static final int INDEX_FILMY_ID_FILMU = 1;
    private static final int INDEX_FILMY_TYTUL = 2;
    private static final int INDEX_FILMY_CZAS_TRWANIA = 3;
    private static final int INDEX_FILMY_OPIS = 4;

    public static final String TABLE_MIEJSCA = "Miejsca";
    public static final String COLUMN_MIEJSCA_ID_MIEJSCA = "id_miejsca";
    public static final String COLUMN_MIEJSCA_ID_SALI = "id_sali";
    public static final String COLUMN_MIEJSCA_ID_FILMU = "id_filmu";
    public static final String COLUMN_MIEJSCA_ID_SEANSU = "id_seansu";
    public static final String COLUMN_MIEJSCA_NR_MIEJSCA = "nr_miejsca";
    public static final String COLUMN_MIEJSCA_RZAD = "rzad";
    public static final int INDEX_MIEJSCA_ID_MIEJSCA = 1;
    public static final int INDEX_MIEJSCA_ID_SALI = 2;
    public static final int INDEX_MIEJSCA_ID_FILMU = 3;
    public static final int INDEX_MIEJSCA_ID_SEASNU = 4;
    public static final int INDEX_MIEJSCA_NR_MIEJESA = 5;
    public static final int INDEX_MIEJSCA_RZAD = 6;

    public static final String TABLE_SALE = "Sale";
    public static final String COLUMN_SALE_ID_SALI = "id_sali";
    public static final String COLUMN_SALE_NAZWA_SALI = "nazwa_sali";
    public static final int INDEX_SALE_ID_SALI = 1;
    public static final int INDEX_SALE_NAZWA_SALI = 2;

    public static final String TABLE_SEANSE = "Seanse";
    public static final String COLUMN_SEANSE_ID_SEASNSU = "id_seansu";
    public static final String COLUMN_SEANSE_ID_FILMU = "id_filmu";
    public static final String COLUMN_SEANSE_ID_SALI = "id_sali";
    public static final String COLUMN_SEASNE_DATA_SEANSU = "data_seansu";
    public static final int INDEX_SEANSE_ID_SEANSU = 1;
    public static final int INDEX_SEANSE_ID_FILMU = 2;
    public static final int INDEX_SEANSE_ID_SALI = 3;
    public static final int INDEX_SEANSE_DATA_SEANSU = 4;

    public static final String TABLE_TRANSAKCJE = "Transakcje";
    public static final String COLUMN_TRANSAKCJE_ID_TRANSAKCJI = "id_transakcji";
    public static final String COLUMN_TRANSAKCJE_WARTOSC_TRANSAKCJI = "wartosc_transakcji";
    public static final int INDEX_TRANSAKCJE_ID_TRANSAKCJI = 1;
    public static final int INDEX_TRANSAKCJE_WARTOSC_TRANSAKCJI = 2;

    public static final String QUERY_FILMY =
            "SELECT * FROM " + TABLE_FILMY + " ORDER BY " + TABLE_FILMY + "." + COLUMN_FILMY_TYTUL;

    private static final String QUERY_FILMY_NA_DATE =
            "SELECT * FROM " + TABLE_FILMY + " WHERE " +  COLUMN_FILMY_ID_FILMU + " in (SELECT " +
                    COLUMN_SEANSE_ID_FILMU + " FROM " + TABLE_SEANSE + " WHERE  date(" +
                    COLUMN_SEASNE_DATA_SEANSU +") =?);";

    public static final String QUERY_SEANSE_DLA_DANEGO_FILMU_BEZ_DATY =
            "SELECT * FROM " + TABLE_SEANSE + " WHERE " + TABLE_SEANSE + "." + COLUMN_SEANSE_ID_FILMU + "=?;";

    public static final String QUERY_SEANSE_DLA_DANEGO_FILMU =
            "SELECT * FROM " + TABLE_SEANSE + " WHERE " + TABLE_SEANSE + "." + COLUMN_SEANSE_ID_FILMU +
                    "=? AND date(" + COLUMN_SEASNE_DATA_SEANSU + ") =?;";

    public static final String QUERY_ZAJETE_MIEJSCA_W_DANYM_SEANSIE =
           "SELECT * FROM " + TABLE_MIEJSCA + " WHERE " + COLUMN_MIEJSCA_ID_MIEJSCA + " IN (SELECT " +
                   COLUMN_BILETY_ID_MIEJSCA + " FROM " + TABLE_BILETY + " WHERE " + COLUMN_BILETY_ID_SEANSU +
                   " =?);";

    public static final String QUERY_MIEJSCA_W_DANYM_SEANSIE =
            "SELECT * FROM " + TABLE_MIEJSCA + " WHERE " + TABLE_MIEJSCA + "." + COLUMN_MIEJSCA_ID_SEANSU + " =?";

    public static final String QUERY_SALE =
            "SELECT * FROM " + TABLE_SALE + " ORDER BY " + TABLE_SALE + "." + COLUMN_SALE_NAZWA_SALI + ";" ;

    public static final String UTWORZ_SEANS =
            "INSERT INTO " + TABLE_SEANSE +
                    " (" + COLUMN_SEANSE_ID_FILMU + ", " + COLUMN_SEANSE_ID_SALI + ", " + COLUMN_SEASNE_DATA_SEANSU +
            ") VALUES(?,?,?)";

    public static final String GENERUJ_MIEJSCA =
            "INSERT INTO " + TABLE_MIEJSCA +
                    " (" + COLUMN_MIEJSCA_ID_SALI + ", " + COLUMN_MIEJSCA_ID_FILMU +
                    ", " + COLUMN_MIEJSCA_ID_SEANSU + ", " + COLUMN_MIEJSCA_NR_MIEJSCA + ", " + COLUMN_MIEJSCA_RZAD +
                    ") VALUES(?,?,?,?,?)";

    public static final String UTWORZ_BILET =
            "INSERT INTO " + TABLE_BILETY +
                    " (" + COLUMN_BILETY_ID_SEANSU + ", " + COLUMN_BILETY_ID_FILMU + ", " + COLUMN_BILETY_ID_SALI + ", " +
                    COLUMN_BILETY_ID_MIEJSCA + ", " + COLUMN_BILETY_ID_TRANSAKCJI + ", " + COLUMN_BILETY_TYP_BILETU +
                    ") VALUES(?,?,?,?,?,?);";

    public static final String UTWORZ_TRANSAKCJE =
            "INSERT INTO " + TABLE_TRANSAKCJE + " (" + COLUMN_TRANSAKCJE_WARTOSC_TRANSAKCJI +
                    ") VALUES(?);";

    public static final String UTWORZ_FILM =
            "INSERT INTO " + TABLE_FILMY +
                    "(" + COLUMN_FILMY_TYTUL + ", " + COLUMN_FILMY_CZAS_TRWANIA + ", " + COLUMN_FILMY_OPIS +
                    ") VALUES(?,?,?);";

    public static final String QUERY_ID_MIEJSCA =
            "SELECT * FROM " + TABLE_MIEJSCA +
                    " WHERE " + TABLE_MIEJSCA + "." + COLUMN_MIEJSCA_ID_SEANSU + " =? AND " +
                    TABLE_MIEJSCA + "." + COLUMN_MIEJSCA_NR_MIEJSCA + "=? AND " +
                    TABLE_MIEJSCA + "." + COLUMN_MIEJSCA_RZAD + "=?;";

    public static final String EDYTUJ_FILM =
            "UPDATE " + TABLE_FILMY +
                    " SET " + COLUMN_FILMY_TYTUL + "=?, " +
                    COLUMN_FILMY_CZAS_TRWANIA + "=?, " +
                    COLUMN_FILMY_OPIS + "=? " +
                    "WHERE " + TABLE_FILMY + "." + COLUMN_FILMY_ID_FILMU + "=?;";

    public static final String USUN_FILM =
            "DELETE FROM " + TABLE_FILMY + " WHERE " + TABLE_FILMY + "." + COLUMN_FILMY_ID_FILMU + " =?;";

    public static final String USUN_SEANS_DLA_DANEGO_FILMU =
            "DELETE FROM " + TABLE_SEANSE + " WHERE " + TABLE_SEANSE + "." + COLUMN_SEANSE_ID_FILMU + " =?;";

    public static final String EDYTUJ_SEANS =
            "UPDATE " + TABLE_SEANSE +
                    " SET " + COLUMN_SEANSE_ID_FILMU + "=?, " +
                    COLUMN_SEANSE_ID_SALI + "=?, " +
                    COLUMN_SEASNE_DATA_SEANSU + "=? " +
                    "WHERE " + TABLE_SEANSE + "." + COLUMN_SEANSE_ID_SEASNSU + "=?;";

    public static final String USUN_SEANS =
            "DELETE FROM " + TABLE_SEANSE + " WHERE " + TABLE_SEANSE + "." + COLUMN_SEANSE_ID_SEASNSU + "=?;";

    public static final String USUN_MIEJSCA_W_DANYM_SEANSIE =
            "DELETE FROM " + TABLE_MIEJSCA + " WHERE " + TABLE_MIEJSCA + "." + COLUMN_MIEJSCA_ID_SEANSU + " =?;";




    private Connection connection;
    private PreparedStatement queryZajeteMiejsca;
    private PreparedStatement generujMiejsca;
    private PreparedStatement utworzTransakcje;
    private PreparedStatement querySeanseDlaDanegoFilmu;
    private PreparedStatement querySeanseDlaDanegoFilmuBezDaty;
    private PreparedStatement queryIdMiejsca;
    private PreparedStatement utworzFilm;
    private PreparedStatement edytujFilm;
    private PreparedStatement usunFilm;
    private PreparedStatement usunSeansDlaDanegoFilmu;
    private PreparedStatement utworzSeans;
    private PreparedStatement edytujSeans;
    private PreparedStatement usunSeans;
    private PreparedStatement usunMiejscaWDanymSeansie;

    private static DatabaseConnector instance = new DatabaseConnector();


    public boolean open(){
        try{
            utworzSeans = connection.prepareStatement(UTWORZ_SEANS, Statement.RETURN_GENERATED_KEYS);
            generujMiejsca = connection.prepareStatement(GENERUJ_MIEJSCA, Statement.RETURN_GENERATED_KEYS);
            queryZajeteMiejsca = connection.prepareStatement(QUERY_ZAJETE_MIEJSCA_W_DANYM_SEANSIE);
            utworzTransakcje = connection.prepareStatement(UTWORZ_TRANSAKCJE, Statement.RETURN_GENERATED_KEYS);
            querySeanseDlaDanegoFilmu = connection.prepareStatement(QUERY_SEANSE_DLA_DANEGO_FILMU);
            queryIdMiejsca = connection.prepareStatement(QUERY_ID_MIEJSCA, Statement.RETURN_GENERATED_KEYS);
            utworzFilm = connection.prepareStatement(UTWORZ_FILM, Statement.RETURN_GENERATED_KEYS);
            usunFilm = connection.prepareStatement(USUN_FILM);
            usunSeansDlaDanegoFilmu = connection.prepareStatement(USUN_SEANS_DLA_DANEGO_FILMU);
            edytujFilm = connection.prepareStatement(EDYTUJ_FILM);
            querySeanseDlaDanegoFilmuBezDaty = connection.prepareStatement(QUERY_SEANSE_DLA_DANEGO_FILMU_BEZ_DATY);
            edytujSeans = connection.prepareStatement(EDYTUJ_SEANS);
            usunSeans = connection.prepareStatement(USUN_SEANS);
            usunMiejscaWDanymSeansie = connection.prepareStatement(USUN_MIEJSCA_W_DANYM_SEANSIE);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void close(){
        try{
            if(connection != null){
                connection.close();
            }
            if(utworzSeans != null){
                utworzSeans.close();
            }
            if(generujMiejsca != null){
                generujMiejsca.close();
            }
            if(queryZajeteMiejsca != null){
                queryZajeteMiejsca.close();
            }
            if(utworzTransakcje != null){
                utworzTransakcje.close();
            }
            if(querySeanseDlaDanegoFilmu != null){
                querySeanseDlaDanegoFilmu.close();
            }
            if(querySeanseDlaDanegoFilmuBezDaty != null){
                querySeanseDlaDanegoFilmuBezDaty.close();
            }
            if(utworzFilm != null){
                utworzFilm.close();
            }
            if(usunFilm != null){
                usunFilm.close();
            }
            if(usunSeansDlaDanegoFilmu != null){
                usunFilm.close();
            }
            if(edytujFilm != null){
                edytujFilm.close();
            }
            if(edytujSeans != null){
                edytujSeans.close();
            }
            if(usunSeans != null){
                usunSeans.close();
            }
            if(usunMiejscaWDanymSeansie != null){
                usunMiejscaWDanymSeansie.close();
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public DatabaseConnector(){
        try {
            this.connection = DriverManager.getConnection("jdbc:sqlite:src/sample/resources/static/" + DB_NAME);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public List<Filmy> queryFilmy(){
        try(Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(QUERY_FILMY)){
            List<Filmy> filmy = new ArrayList<>();
            while (resultSet.next()){
                Filmy film = new Filmy();
                film.setIdFilmu(resultSet.getInt(INDEX_FILMY_ID_FILMU));
                film.setCzasTrwania(resultSet.getString(INDEX_FILMY_CZAS_TRWANIA));
                film.setOpis(resultSet.getString(INDEX_FILMY_OPIS));
                film.setTytul(resultSet.getString(INDEX_FILMY_TYTUL));

                filmy.add(film);
            }
            return filmy;
        } catch (SQLException exe){
            System.out.println("Zapytanie zakonczone niepowodzeniem: " + exe.getMessage());
            exe.printStackTrace();
            return null;
        }
    }

    public List<Sale> querySale(){
        try(Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(QUERY_SALE)){
            List<Sale> sale = new ArrayList<>();
            while (resultSet.next()){
                Sale sala = new Sale();
                sala.setIdSali(resultSet.getInt(INDEX_SALE_ID_SALI));
                sala.setNazwaSali(resultSet.getString(INDEX_SALE_NAZWA_SALI));
                sale.add(sala);
            }
            return sale;
        } catch (SQLException exe){
            System.out.println("Zapytanie zakonczone niepowodzeniem: " + exe.getMessage());
            exe.printStackTrace();
            return null;
        }
    }

    public List<Filmy> queryFilmyDlaDaty(Date data){
        PreparedStatement statement = null;
        try{
            statement  = connection.prepareStatement(QUERY_FILMY_NA_DATE);
            statement.setString(1, data.toString());
        }catch (Exception e){
            e.getMessage();
        }

        try(ResultSet resultSet = statement.executeQuery()){
            List<Filmy> filmy = new ArrayList<>();
            while (resultSet.next()){
                Filmy film = new Filmy();
                film.setIdFilmu(resultSet.getInt(INDEX_FILMY_ID_FILMU));
                film.setCzasTrwania(resultSet.getString(INDEX_FILMY_CZAS_TRWANIA));
                film.setOpis(resultSet.getString(INDEX_FILMY_OPIS));
                film.setTytul(resultSet.getString(INDEX_FILMY_TYTUL));

                filmy.add(film);
            }
            return filmy;
        } catch (SQLException exe){
            System.out.println("Zapytanie zakonczone niepowodzeniem: " + exe.getMessage());
            exe.printStackTrace();
            return null;
        }
    }

    public List<Seanse> querySeansDlaDanegoFilmu(Filmy film, Date dataSeansu){
        List<Seanse> seanse = new ArrayList<>();
        try {
            querySeanseDlaDanegoFilmu.setInt(1, film.getIdFilmu());
            querySeanseDlaDanegoFilmu.setString(2, dataSeansu.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return wyślijZapytanieOSeanse(seanse, querySeanseDlaDanegoFilmu);
    }

    public List<Seanse> querySeanseDlaDanegoFilmuBezDaty(int idFilmu){
        List<Seanse> seanse = new ArrayList<>();
        try {
            querySeanseDlaDanegoFilmuBezDaty.setInt(1, idFilmu);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wyślijZapytanieOSeanse(seanse, querySeanseDlaDanegoFilmuBezDaty);

    }

    private List<Seanse> wyślijZapytanieOSeanse(List<Seanse> seanse, PreparedStatement query) {
        try(ResultSet resultSet = query.executeQuery()) {
            while (resultSet.next()) {
                Seanse seans = new Seanse();

                seans.setIdSeansu(resultSet.getInt(INDEX_SEANSE_ID_SEANSU));
                seans.setIdFilmu(resultSet.getInt(INDEX_SEANSE_ID_FILMU));
                seans.setIdSali(resultSet.getInt(INDEX_SEANSE_ID_SALI));
                String dataString = resultSet.getString(INDEX_SEANSE_DATA_SEANSU);

                SimpleDateFormat data = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                try {
                    seans.setDataSeansu(data.parse(dataString));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                seanse.add(seans);
            }
            return seanse;
        } catch (SQLException exe){
            System.out.println("Zapytanie zakonczone niepowodzeniem " + exe.getMessage());
            exe.printStackTrace();
            return null;
        }
    }

    public void utworzFilm(String tytul, String czasTrwania, String opis) throws SQLException{

        utworzFilm.setString(1, tytul);
        utworzFilm.setString(2, czasTrwania);
        utworzFilm.setString(3, opis);


        int affected = utworzFilm.executeUpdate();
        if(affected !=1) {
            throw new SQLException("Nie dodano filmu");
        }
    }

    public void edytujFilm(int idFilmu, String tytul, String czasTrwania, String opis) throws SQLException{
        edytujFilm.setString(1, tytul);
        edytujFilm.setString(2, czasTrwania);
        edytujFilm.setString(3, opis);
        edytujFilm.setInt(4, idFilmu);

        edytujFilm.executeUpdate();

    }

    public void usunFilm(int idFilmu) throws SQLException{

        usunFilm.setInt(1, idFilmu);
        usunFilm.executeUpdate();

        usunSeansDlaDanegoFilmu.setInt(1,idFilmu);
        usunSeansDlaDanegoFilmu.executeUpdate();
    }


    public int utworzSeans(int idFilmu, int idSali, String dataSeansu) throws SQLException{

        utworzSeans.setInt(1,idFilmu);
        utworzSeans.setInt(2, idSali);
        utworzSeans.setString(3, dataSeansu);

        int affected = utworzSeans.executeUpdate();
        if(affected != 1) {
            throw new SQLException("Nie dodano seansu!");
        }
        ResultSet generatedKeys = utworzSeans.getGeneratedKeys();
        if(generatedKeys.next()){
            int idSeansu = generatedKeys.getInt(1);
            generujMiejsca(idSali, idFilmu, idSeansu);
            return idSeansu;
        } else {
            throw new SQLException("Nie mozna było zdobyć idSeansu!");
        }
    }

    public void edytujSeans(int idSeansu, int idFilmu, int idSali, String dataSeansu) throws SQLException {
        edytujSeans.setInt(1, idFilmu);
        edytujSeans.setInt(2, idSali);
        edytujSeans.setString(3, dataSeansu);
        edytujSeans.setInt(4, idSeansu);
        edytujSeans.executeUpdate();
    }

    public void usunSeans(int idSeansu) throws SQLException{

        usunMiejscaWDanymSeansie(idSeansu);
        usunSeans.setInt(1, idSeansu);
        usunSeans.executeUpdate();
    }

    public void usunMiejscaWDanymSeansie(int idSeansu) throws SQLException{
        usunMiejscaWDanymSeansie.setInt(1,idSeansu);
        usunMiejscaWDanymSeansie.executeUpdate();

    }


    private void generujMiejsca(int idSali, int idFilmu, int idSeansu){
        try {
            int i =1;
            for(int j=1; j<=10 ;j++){
                for(int k=1; k<=15; k++){
                    generujMiejsca.setInt(1, idSali);
                    generujMiejsca.setInt(2, idFilmu);
                    generujMiejsca.setInt(3, idSeansu);
                    generujMiejsca.setInt(4, k);
                    generujMiejsca.setInt(5, j);

                    generujMiejsca.executeUpdate();
                    i++;
                }
            }
        } catch (SQLException exe){
            System.out.println("Zapytanie zakonczone niepowodzeniem: " + exe.getMessage());
        }
    }

    public int utworzTransakcje() throws SQLException{
        utworzTransakcje.setInt(1, 0); //ustawienie wartosci transakcji na zero

        int affected = utworzTransakcje.executeUpdate();
        if(affected != 1){
            throw new SQLException("Nie dodano transakcji!");
        }
        ResultSet generatedKeys = utworzTransakcje.getGeneratedKeys();
        if(generatedKeys.next()){
            return generatedKeys.getInt(1);
        } else {
            throw new SQLException("Nie wygenerowano idTransakcji!");
        }
    }

    public List<Miejsca> znajdzZajeteMiejsca(Seanse seans){
        List<Miejsca> miejsca = new ArrayList<>();

        try {
            queryZajeteMiejsca.setInt(1, seans.getIdSeansu());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try(ResultSet resultSet = queryZajeteMiejsca.executeQuery()){
            while(resultSet.next()){
                Miejsca miejsce = new Miejsca();
                miejsce.setIdMiejsca(resultSet.getInt(INDEX_MIEJSCA_ID_MIEJSCA));
                miejsce.setIdSali(resultSet.getInt(INDEX_MIEJSCA_ID_SALI));
                miejsce.setIdFilmu(resultSet.getInt(INDEX_MIEJSCA_ID_FILMU));
                miejsce.setIdSeansu(resultSet.getInt(INDEX_MIEJSCA_ID_SEASNU));
                miejsce.setNrMiejsca(resultSet.getInt(INDEX_MIEJSCA_NR_MIEJESA));
                miejsce.setRzad(resultSet.getInt(INDEX_MIEJSCA_RZAD));

                miejsca.add(miejsce);
            }
            return miejsca;

        } catch (SQLException exe){
            System.out.println("Zapytanie zakonczone niepowodzeniem " + exe.getMessage());
            exe.printStackTrace();
            return null;
        }
    }

    public Miejsca znajdzMiejsce(Seanse seans, int nrMiejsca, int rzad){

        Miejsca miejsce = new Miejsca();

        try {
            queryIdMiejsca.setInt(1, seans.getIdSeansu());
            queryIdMiejsca.setInt(2, nrMiejsca);
            queryIdMiejsca.setInt(3, rzad);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try(ResultSet resultSet = queryIdMiejsca.executeQuery()){
            while(resultSet.next()){
                miejsce.setIdMiejsca(resultSet.getInt(INDEX_MIEJSCA_ID_MIEJSCA));
                miejsce.setIdSali(resultSet.getInt(INDEX_MIEJSCA_ID_SALI));
                miejsce.setIdFilmu(resultSet.getInt(INDEX_MIEJSCA_ID_FILMU));
                miejsce.setIdSeansu(resultSet.getInt(INDEX_MIEJSCA_ID_SEASNU));
                miejsce.setNrMiejsca(resultSet.getInt(INDEX_MIEJSCA_NR_MIEJESA));
                miejsce.setRzad(resultSet.getInt(INDEX_MIEJSCA_RZAD));

            }
            return miejsce;

        } catch (SQLException exe){
            System.out.println("Zapytanie zakonczone niepowodzeniem " + exe.getMessage());
            exe.printStackTrace();
            return null;
        }
    }

    public int idDlaNowejTransakcji(){
        String query = "SELECT MAX(id_transakcji) FROM Transakcje;";
        int noweId = -1;

        try {
            PreparedStatement stm = connection.prepareStatement(query);
            ResultSet resultSet = stm.executeQuery();
            noweId = resultSet.getInt(INDEX_TRANSAKCJE_ID_TRANSAKCJI) + 1;
        }catch (SQLException e){
            e.printStackTrace();
        }

        return noweId;
    }

    public int idDlaNowegoBiletu(){
        String query = "SELECT MAX(id_biletu) FROM Bilety;";
        int noweId = -1;

        try {
            PreparedStatement stm = connection.prepareStatement(query);
            ResultSet resultSet = stm.executeQuery();
            noweId = resultSet.getInt(INDEX_BILETY_ID_BILETU) + 1;
        }catch (SQLException e){
            e.printStackTrace();
        }

        return noweId;
    }


    public void insertTransakcja(Transakcje transakcja) {
        String query = "INSERT INTO TRANSAKCJE (id_transakcji, wartosc_transakcji) VALUES (? , ?);";
        try{
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setInt(1, transakcja.getIdTransakcji());
            stm.setInt(2, transakcja.getWartoscTransakcji());

            stm.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void insertBilet(Bilety bilet) {
        String query = "INSERT INTO BILETY (id_biletu, id_filmu, id_miejsca, id_sali, id_seansu, id_transakcji, typ_biletu) VALUES (?, ?, ?, ?, ?, ?, ?);";

        try{
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setInt(1, bilet.getIdBiletu());
            stm.setInt(2, bilet.getIdFilmu());
            stm.setInt(3,bilet.getIdMiejsca());
            stm.setInt(4, bilet.getIdSali());
            stm.setInt(5, bilet.getIdSeansu());
            stm.setInt(6, bilet.getIdTransakcji());
            stm.setInt(7, Integer.parseInt(bilet.getTypBiletu()));

            stm.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public List<Transakcje> pobierzTransakcje(int strona){
        String query = "SELECT * FROM Transakcje LIMIT 25 OFFSET ?;";
        List<Transakcje> transakcje = new ArrayList<>();

        try{
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setInt(1, (strona - 1) * 25);
            ResultSet resultSet = stm.executeQuery();
            while (resultSet.next()){
                Transakcje t = new Transakcje();
                t.setIdTransakcji(resultSet.getInt(INDEX_TRANSAKCJE_ID_TRANSAKCJI));
                t.setWartoscTransakcji(resultSet.getInt(INDEX_TRANSAKCJE_WARTOSC_TRANSAKCJI));
                transakcje.add(t);
            }

            return transakcje;

        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public int liczbaStronTransakcji(){
        String query = "SELECT COUNT (*) FROM Transakcje;";

        try{
            Statement stm = connection.createStatement();
            ResultSet resultSet = stm.executeQuery(query);
            int liczbaTransakcji = resultSet.getInt(1);
            return (liczbaTransakcji / 25) + 1;

        }catch(SQLException e){
            e.printStackTrace();
            return 0;
        }
    }

    public List<Bilety> pobierzBiletyDlaTransakcji(int idTransakcji, int strona){
        String query = "SELECT * FROM Bilety WHERE id_transakcji =? LIMIT 25 OFFSET ?;";
        List<Bilety> bilety = new ArrayList<>();

        try{
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setInt(1,idTransakcji);
            stm.setInt(2, (strona - 1) * 25);
            ResultSet resultSet = stm.executeQuery();

            while (resultSet.next()){
                Bilety b = new Bilety();
                b.setIdBiletu(resultSet.getInt(INDEX_BILETY_ID_BILETU));
                b.setIdSeansu(resultSet.getInt(INDEX_BILETY_ID_SEANSU));
                b.setIdFilmu(resultSet.getInt(INDEX_BILETY_ID_FILMU));
                b.setIdSali(resultSet.getInt(INDEX_BILETY_ID_SALI));
                b.setIdMiejsca(resultSet.getInt(INDEX_BILETY_ID_MIEJSCA));
                b.setIdTransakcji(resultSet.getInt(INDEX_BILETY_ID_TRANSAKCJI));
                b.setTypBiletu(Integer.toString(resultSet.getInt(INDEX_BILETY_TYP_BILETU)));

                bilety.add(b);
            }

            return bilety;
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }


    }

    public int liczbaStronBiletów(int idTransakcji){
        String query = "SELECT COUNT (*) FROM Bilety WHERE id_transakcji =?;";

        try{
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setInt(1, idTransakcji);
            ResultSet resultSet = stm.executeQuery();
            int liczbaBiletów = resultSet.getInt(1);
            return (liczbaBiletów / 25) + 1;

        }catch(SQLException e){
            e.printStackTrace();
            return 0;
        }
    }

    public void deleteBiletyDlaTransakcji(int id_transakcji){
        String query = "DELETE FROM Bilety WHERE id_transakcji =?;";

        try{
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setInt(1,id_transakcji);
            stm.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void deleteTransakcję(int id_transakcji){
        String query = "DELETE FROM Transakcje WHERE id_transakcji =?;";

        try{
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setInt(1,id_transakcji);
            stm.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public PDFDataModel przygotujJedenBiletDoDruku(Bilety bilet, Transakcje transakcja){
        PDFDataModel model = new PDFDataModel();
        model.bilety = new ArrayList<>();
        model.bilety.add(bilet);
        model.transakcja = transakcja;

        String query = "SELECT * FROM Filmy WHERE id_filmu =" + bilet.getIdFilmu() + ";";
        try{
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery(query);
            model.film = new Filmy();
            model.film.setIdFilmu(rs.getInt(INDEX_FILMY_ID_FILMU));
            model.film.setTytul(rs.getString(INDEX_FILMY_TYTUL));
            model.film.setCzasTrwania(rs.getString(INDEX_FILMY_CZAS_TRWANIA));
            model.film.setOpis(rs.getString(INDEX_FILMY_OPIS));
        }catch(SQLException e){
            e.printStackTrace();
        }

        query = "SELECT * FROM Seanse WHERE id_seansu=" + bilet.getIdSeansu() + ";";
        try{
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery(query);
            model.seans = new Seanse();
            model.seans.setIdSeansu(rs.getInt(INDEX_SEANSE_ID_SEANSU));
            model.seans.setIdFilmu(rs.getInt(INDEX_SEANSE_ID_FILMU));
            model.seans.setIdSali(rs.getInt(INDEX_SEANSE_ID_SALI));
            String dataString = rs.getString(INDEX_SEANSE_DATA_SEANSU);
            SimpleDateFormat data = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                model.seans.setDataSeansu(data.parse(dataString));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }catch(SQLException e){
            e.printStackTrace();
        }

        model.miejsca = new ArrayList<>();
        query = "SELECT * FROM Miejsca WHERE id_miejsca =" + bilet.getIdMiejsca() + ";";
        try{
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery(query);
            Miejsca m = new Miejsca();
            m.setIdMiejsca(rs.getInt(INDEX_MIEJSCA_ID_MIEJSCA));
            m.setIdSali(rs.getInt(INDEX_MIEJSCA_ID_SALI));
            m.setIdFilmu(rs.getInt(INDEX_MIEJSCA_ID_FILMU));
            m.setIdSeansu(rs.getInt(INDEX_MIEJSCA_ID_SEASNU));
            m.setNrMiejsca(rs.getInt(INDEX_MIEJSCA_NR_MIEJESA));
            m.setRzad(rs.getInt(INDEX_MIEJSCA_RZAD));

            model.miejsca.add(m);
        }catch (SQLException e){
            e.printStackTrace();
        }

        return model;
    }

    public void deleteSale(int id_sali){
        String query = "DELETE FROM Sale WHERE id_sali =?;";

        try{
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setInt(1,id_sali);
            stm.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void insertSala(String nazwa_sali){
        String query = "INSERT INTO Sale (nazwa_sali) VALUES (?);";

        try{
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setString(1, nazwa_sali);
            stm.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void updateSala(int id_sali, String nazwa_sali){
        String query = "UPDATE Sale SET nazwa_sali =? WHERE id_sali = ?;";
        try{
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setString(1, nazwa_sali);
            stm.setInt(2,id_sali);
            stm.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public Connection getConnection(){
        return this.connection;
    }

    public static DatabaseConnector getInstance() {
        return instance;
    }
}