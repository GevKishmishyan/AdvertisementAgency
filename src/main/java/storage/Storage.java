package storage;

import exception.ExistingModelException;
import exception.ModelNotFoundException;
import model.Ad;
import model.Category;
import model.User;

import java.io.IOException;

public interface Storage {

    void add(User user) throws ExistingModelException, IOException;

    void add(Ad ad) throws ExistingModelException, IOException;

    boolean isUsersEmpty();

    boolean isAdsEmpty();

    void printAllAds();

    void printAdByCategory(Category category);

    void printAllAdsByTitleSort();

    void printAllAdsByDateSort();

    void deleteAllAdsByUser(User user) throws IOException;

    void deleteAdByTitle(String title, User currentUser) throws IOException;


}
