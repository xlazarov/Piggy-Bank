
import data.CategoryDao;
import data.StatisticDao;
import data.TransactionDao;
import ui.MainFrame;

import javax.sql.DataSource;
import javax.swing.UIManager;
import java.awt.EventQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.derby.jdbc.EmbeddedDataSource;

public class Main {

    public static void main(String[] args) {
        CategoryDao categoryDao = new CategoryDao(createDataSource());
        TransactionDao transactionDao = new TransactionDao(createDataSource());
        StatisticDao statisticDao = new StatisticDao(createDataSource());
        initNimbusLookAndFeel();
        EventQueue.invokeLater(() -> new MainFrame(categoryDao, transactionDao, statisticDao).setVisible(true));
    }

    private static DataSource createDataSource() {
        String dbPath = System.getProperty("user.home") + "/piggy-bank";
        EmbeddedDataSource dataSource = new EmbeddedDataSource();
        dataSource.setDatabaseName(dbPath);
        dataSource.setCreateDatabase("create");
        return dataSource;
    }

    private static void initNimbusLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Nimbus layout initialization failed", ex);
        }
    }
}
