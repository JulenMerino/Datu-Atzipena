package DatuAtzipena;

import java.sql.*;

/**
 * MySQLDB klaseak MySQL datu-basearekin konektatzeko eta datuak kudeatzeko funtzioak eskaintzen ditu.
 * Klase honek MySQL datu-basean eragiketak (insert, delete, select) egin ditzake.
 */
public class MySQLDB {

    private Connection connection;

    /**
     * MySQLDB konstruktorea.
     * Hasieran, konexioa null balioarekin hasten da.
     */
    public MySQLDB() {
        this.connection = null;
    }

    /**
     * MySQL datu-basearekin konektatzen saiatzen da.
     * Konektatzeko helbidea "jdbc:mysql://localhost:3306/fitxategikonbinatuak" erabiliz adierazten da,
     * eta erabiltzailearen izena eta pasahitza zehazten ditu.
     * 
     * @throws SQLException SQL errorea gertatzen bada konexioa ezartzean
     */
    public void konektatu() {
        try {
            String url = "jdbc:mysql://localhost:3306/fitxategikonbinatuak";
            String usuario = "root";
            String contrasena = "mysql";
            connection = DriverManager.getConnection(url, usuario, contrasena);
            System.out.println("MySQL-ra konexioa eginda.");
        } catch (SQLException e) {
            System.out.println("Konexio errorea: " + e.getMessage());
        }
    }

    /**
     * Enpresa eta langileak taulatan datuak sartzen ditu.
     * MySQL datu-baseko "enpresa" eta "langilea" taulak betetzen ditu.
     * 
     * @throws SQLException SQL errorea gertatzen bada datuak sartzean
     */
    public void datuenInsert() {
        try {
            // Enpresa taularen datuak
            String sqlEnpresa = "INSERT INTO enpresa (idEnpresa, Izena, Sektorea, PertsonaKop, Telefonoa, Email, Helbidea, Hiria) VALUES " +
                                "(1, 'TecnoSoluciones S.A.', 'Tecnología', 120, '+52 55 1234 5678', 'contacto@tecnosoluciones.com', 'Av. Insurgentes Sur 1234, Piso 4', 'Ciudad de México'), " +
                                "(2, 'VerdeVida Agroindustrial', 'Agricultura', 80, '+57 1 9876 5432', 'info@verdevida.co', 'Calle 45 No. 23-56', 'Bogotá'), " +
                                "(3, 'SaludVital Clínicas', 'Salud', 50, '+34 910 234 567', 'contacto@saludvital.es', 'Calle Mayor 89, Planta 2', 'Madrid'), " +
                                "(4, 'Logística Global Express', 'Transporte y Logística', 150, '+1 212 345 6789', 'support@globalexpress.com', '456 7th Avenue', 'Nueva York');";

            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate(sqlEnpresa);
                System.out.println("Enpresa taulan datuak sartuta");
            }

            // Langileak taularen datuak
            String sqlLangilea = "INSERT INTO langilea (Abizena, Izena, Kargua, Tratua, JaiotzeData, KontratazioData, Helbidea, Hiria, Lanean, Enpresa_idEnpresa) VALUES " +
                                 "('Davolio', 'Nancy', 'Representante de ventas', 'Stra.', '1968-12-08', '1992-05-01', '507-20th Ave.E.', 'Seattle', true, 4), " +
                                 "('Fuller', 'Andrew', 'Vicepresidente comercial', 'Dr.', '1952-02-19', '1992-08-14', '908 W. Capital Way', 'Tacoma', true, 3), " +
                                 "('Leverling', 'Janet', 'Representante de ventas', 'Stra.', '1963-08-30', '1992-04-01', '722 Moss Bay Blvd.', 'Kirkland', true, 3), " +
                                 "('Peacock', 'Margaret', 'Representante de ventas', 'Sra.', '1958-09-19', '1993-05-03', '4110 Old Redmond Rd.', 'Redmond', false, 3), " +
                                 "('Buchanan', 'Steven', 'Gerente de ventas', 'Sr.', '1955-03-04', '1993-10-17', '14 Garrett Hill', 'Londres', true, 2), " +
                                 "('Suyama', 'Michel', 'Representante de ventas', 'Sr.', '1963-07-02', '1993-10-17', 'Coventry House', 'Londres', true, 1), " +
                                 "('King', 'Robert', 'Representante de ventas', 'Sr.', '1960-05-29', '1994-01-02', 'Edgeham Hollow', 'Londres', false, 1), " +
                                 "('Callahan', 'Laura', 'Coordinador ventas internacionales', 'Stra.', '1958-01-09', '1994-03-05', '4726-11th Ave.N.E.', 'Seattle', true, 4), " +
                                 "('Dodsworth', 'Anne', 'Representante de ventas', 'Stra.', '1969-07-02', '1994-11-02', '7 Houndstooth Rd.', 'Londres', true, 2);";

            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate(sqlLangilea);
                System.out.println("Langileak taulan datuka sartuta");
            }

        } catch (SQLException e) {
            System.out.println("INSERT egitean errorea: " + e.getMessage());
        }
    }

    /**
     * Enpresa eta langileak taulako datuak ezabatzen ditu.
     * "enpresa" eta "langilea" taulak hutsik uzten ditu.
     * 
     * @throws SQLException SQL errorea gertatzen bada datuak ezabatzean
     */
    public void datuenDelete() {
        String sqlEnpresa = "DELETE FROM enpresa";
        String sqlLangilea = "DELETE FROM langilea";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sqlEnpresa);
            System.out.println("Enpresa taulatik datuak ezabatuta");

            stmt.executeUpdate(sqlLangilea);
            System.out.println("Langieak taulatik datuak ezabatuta");
        } catch (SQLException e) {
            System.out.println("DELETE egitean errorea: " + e.getMessage());
        }
    }

    /**
     * Enpresa eta langileak taulako datuak aukeratzen ditu eta inprimatzen ditu.
     * Langile bakoitza enpresaren arabera antolatzen da.
     * 
     * @throws SQLException SQL errorea gertatzen bada datuak aukeratzean
     */
    public void datuenSelect() {
        String query = "SELECT " +
                       "langilea.idLangilea AS langilea_id, " +
                       "langilea.Abizena, " +
                       "langilea.Izena, " +
                       "langilea.Kargua, " +
                       "langilea.Tratua, " +
                       "langilea.JaiotzeData, " +
                       "langilea.KontratazioData, " +
                       "langilea.Helbidea AS langilea_helbidea, " +
                       "langilea.Hiria AS langilea_hiria, " +
                       "langilea.Lanean, " +
                       "langilea.Enpresa_idEnpresa AS langilea_enpresa_id, " +
                       "enpresa.idEnpresa AS enpresa_id, " +
                       "enpresa.Izena AS enpresa_izena, " +
                       "enpresa.Sektorea, " +
                       "enpresa.PertsonaKop, " +
                       "enpresa.Telefonoa, " +
                       "enpresa.Email, " +
                       "enpresa.Helbidea AS enpresa_helbidea, " +
                       "enpresa.Hiria AS enpresa_hiria " +
                       "FROM langilea " +
                       "INNER JOIN enpresa ON langilea.Enpresa_idEnpresa = enpresa.idEnpresa " +
                       "ORDER BY enpresa_izena";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            String azkenEnpresa = "";

            while (rs.next()) {
                String unekoEnpresa = rs.getString("enpresa_izena");

                if (!unekoEnpresa.equals(azkenEnpresa)) {
                    if (!azkenEnpresa.isEmpty()) {
                        System.out.println("---------------------------------------------------------");
                    }

                    System.out.println("******************** ENPRESA: " + unekoEnpresa + " ********************");
                    azkenEnpresa = unekoEnpresa;
                }

                System.out.println();
                System.out.println("\tLangilea ID: " + rs.getInt("langilea_id"));
                System.out.println("\tLangilearen Abizena: " + rs.getString("Abizena"));
                System.out.println("\tLangilearen Izena: " + rs.getString("Izena"));
                System.out.println("\tLangilearen Kargua: " + rs.getString("Kargua"));
                System.out.println("\tLangilearen Tratua: " + rs.getString("Tratua"));
                System.out.println("\tLangilearen Jaiotze Data: " + rs.getString("JaiotzeData"));
                System.out.println("\tLangilearen Kontratazio Data: " + rs.getString("KontratazioData"));
                System.out.println("\tLangilearen Helbidea: " + rs.getString("langilea_helbidea"));
                System.out.println("\tLangilearen Hiria: " + rs.getString("langilea_hiria"));
                System.out.println("\tLangilea Landean Dago: " + rs.getString("Lanean"));
            }

            if (azkenEnpresa.isEmpty()) {
                System.out.println("Ez da erregistrorik aurkitu.");
            } else {
                System.out.println("---------------------------------------------------------");
                System.out.println("SELECT kontsulta eginda");
            }

        } catch (SQLException e) {
            System.out.println("SELECT egitean errorea: " + e.getMessage());
        }
    }

    /**
     * Datu-basearekin egiten den konexioa itxi egiten du.
     * Konexioa ixten da, SQL erroreak ezabatzen saiatuz.
     * 
     * @throws SQLException SQL errorea gertatzen bada konexioa ixtean
     */
    public void itxi() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Konexioa ixten.");
            }
        } catch (SQLException e) {
            System.out.println("Konexioa ixtean erroera: " + e.getMessage());
        }
    }
}
