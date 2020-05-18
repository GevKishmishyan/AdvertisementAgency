package storage.Impl;

import exception.ExistingModelException;
import model.Ad;
import model.Category;
import model.User;
import storage.Storage;
import util.FileUtil;

import java.util.*;

public class StorageImpl implements Storage {

    private static long adId = 1;

    private Map<String, User> users = new HashMap<>();
    private List<Ad> ads = new ArrayList<>();

    public void initData() {
        users = FileUtil.deserializeUserMap();
        ads = FileUtil.deserializeAdList();
        if (ads != null && !ads.isEmpty()) {
            Ad ad = ads.get(ads.size() - 1);
            adId = ad.getId() + 1;
        }
    }

    @Override
    public void add(User user) throws ExistingModelException {
        if (getUserByPhoneNumber(user.getPhoneNumber()) != null) {
            throw new ExistingModelException(String.format("User with \"%s\" phoneNumber is already exist.", user.getPhoneNumber()));
        }
        users.put(user.getPhoneNumber(), user);
        FileUtil.serializeUserMap(users);
    }

    @Override
    public void add(Ad ad) throws ExistingModelException {
        if (getAdByTitle(ad) != null) {
            throw new ExistingModelException(String.format("Ad with \"%s\" title is already exist.", ad.getTitle()));
        }
        ad.setId(adId++);
        ads.add(ad);
        FileUtil.serializeAdList(ads);
    }


    public User getUserByPhoneNumber(String phoneNumber) {
        return users.get(phoneNumber);
    }

    public Ad getAdByTitleStr(String title) {
        for (Ad ad1 : ads) {
            if (ad1.getTitle().equals(title)) {
                return ad1;
            }
        }
        return null;
    }

    private Ad getAdByTitle(Ad ad) {
        for (Ad ad1 : ads) {
            if (ad1.getTitle().equals(ad.getTitle())) {
                return ad1;
            }
        }
        return null;
    }

    @Override
    public boolean isUsersEmpty() {
        return users.isEmpty();
    }

    @Override
    public boolean isAdsEmpty() {
        return ads.isEmpty();
    }

    public void printAllUsers() {
        for (User user : users.values()) {
            System.out.println(user);
        }

    }

    public void printMyAllAds(User user) {
        for (Ad ad : ads) {
            if (ad.getAuthor().equals(user)) {
                System.out.println(ad);
            }
        }
    }

    @Override
    public void printAllAds() {
        for (Ad ad : ads) {
            System.out.println(ad);
        }
    }

    @Override
    public void printAdByCategory(Category category) {
        for (Ad ad : ads) {
            if (ad.getCategory() == category) {
                System.out.println(ad);
            }
        }
    }

    @Override
    public void printAllAdsByTitleSort() {
        ArrayList<Ad> orderedAds = new ArrayList<>(this.ads);
        Collections.sort(orderedAds);
        for (Ad ad : orderedAds) {
            System.out.println(ad);
        }
    }

    @Override
    public void printAllAdsByDateSort() {
        ArrayList<Ad> orderedAds = new ArrayList<>(this.ads);
        orderedAds.sort(new Comparator<Ad>() {
            @Override
            public int compare(Ad o1, Ad o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });
        for (Ad ad : orderedAds) {
            System.out.println(ad);
        }
    }

    @Override
    public void deleteAllAdsByUser(User user) {
        Iterator iterator = ads.iterator();
        while (iterator.hasNext()) {
            Ad ad = (Ad) iterator.next();
            if (ad.getAuthor().equals(user)) {
                iterator.remove();
            }
        }
        FileUtil.serializeAdList(ads);
    }

    @Override
    public void deleteAdByTitle(String title, User currentUser) {
        for (int i = 0; i < ads.size(); i++) {
            Ad ad = ads.get(i);
            if (ad.getTitle().equals(title) && ad.getAuthor().equals(currentUser)) {
                ads.remove(i);
            }
        }
        FileUtil.serializeAdList(ads);
    }

    public List<Ad> getAds() {
        return ads;
    }
}
