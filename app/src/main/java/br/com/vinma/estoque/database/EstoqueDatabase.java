package br.com.vinma.estoque.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import br.com.vinma.estoque.database.converter.BigDecimalConverter;
import br.com.vinma.estoque.database.dao.ProductDAO;
import br.com.vinma.estoque.model.Produto;

@Database(entities = {Produto.class}, version = 3, exportSchema = false)
@TypeConverters(value = {BigDecimalConverter.class})
public abstract class EstoqueDatabase extends RoomDatabase {

    private static final String DB_NAME = "estoque.db";

    public abstract ProductDAO getProductDAO();

    public static EstoqueDatabase getInstance(Context context) {
        return Room.databaseBuilder(
                context,
                EstoqueDatabase.class,
                        DB_NAME)
                .addMigrations(
                    new Migration[]{
                            new Migration(1, 2) {
                                // Migrate to english
                                @Override
                                public void migrate(@NonNull SupportSQLiteDatabase database) {
                                    // copy current database to new database
                                    database.execSQL("CREATE TABLE IF NOT EXISTS `Product` " +
                                            "(`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                                            "`name` TEXT," +
                                            "`price` REAL," +
                                            "`stock` INTEGER NOT NULL)");
                                    // insert old data to new database
                                    database.execSQL("INSERT INTO `Product` (`id`, `name`, `price`, `stock`) " +
                                            "SELECT `id`, `nome`, `preco`, `quantidade` FROM `Produto`");
                                    // drop old database
                                    database.execSQL("DROP TABLE `Produto`");
                                }
                            },
                            new Migration(2, 3) {
                                // migrate back to portuguese
                                @Override
                                public void migrate(@NonNull SupportSQLiteDatabase database) {
                                    // copy current database to new database
                                    database.execSQL("CREATE TABLE IF NOT EXISTS `Produto` " +
                                            "(`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                                            "`nome` TEXT," +
                                            "`preco` REAL," +
                                            "`quantidade` INTEGER NOT NULL)");
                                    // insert old data to new database
                                    database.execSQL("INSERT INTO `Produto` (`id`, `nome`, `preco`, `quantidade`) " +
                                            "SELECT `id`, `name`, `price`, `stock` FROM `Product`");
                                    // drop old database
                                    database.execSQL("DROP TABLE `Product`");
                                }
                            }
                    }).build();
    }
}
