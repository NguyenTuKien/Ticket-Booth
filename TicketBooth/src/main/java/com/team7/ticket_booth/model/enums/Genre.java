package com.team7.ticket_booth.model.enums;

import lombok.Getter;

@Getter
public enum Genre {
    ACTION("Hành động"),
    ADVENTURE("Phiêu lưu"),
    COMEDY("Hài"),
    DRAMA("Tâm lý"),
    HORROR("Kinh dị"),
    THRILLER("Giật gân"),
    CRIME("Tội phạm"),
    ROMANCE("Tình cảm"),
    ANIMATION("Hoạt hình"),
    FANTASY("Giả tưởng"),
    SCI_FI("Khoa học viễn tưởng"),
    DOCUMENTARY("Tài liệu"),
    WAR("Chiến tranh"),
    HISTORICAL("Lịch sử"),
    MUSICAL("Âm nhạc"),
    FAMILY("Gia đình"),
    SPORTS("Thể thao");

    private final String description;

    Genre(String description) {
        this.description = description;
    }

    public static Genre findByDescription(String description) {
        for (Genre genre : values()) {
            if (genre.getDescription().equals(description)) {
                return genre;
            }
        }
        return null;
    }
}
