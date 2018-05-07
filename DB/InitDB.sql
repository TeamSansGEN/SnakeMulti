SET foreign_key_checks = 0;

DROP SCHEMA IF EXISTS snakemulti;
CREATE SCHEMA snakemulti;
USE snakemulti;

CREATE TABLE IF NOT EXISTS Users (
	user_id INT NOT NULL AUTO_INCREMENT,
	username VARCHAR(64) NOT NULL,
    password VARCHAR(64) NOT NULL,					# hash SHA-256
    salt VARCHAR(64) NOT NULL,					
    privilegeLevel TINYINT NOT NULL DEFAULT 0,		# 0 = defaultPlayer, 1 = admin
    PRIMARY KEY(user_id)
);

CREATE TABLE IF NOT EXISTS Leaderboard (
	id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    games_played INT NOT NULL DEFAULT 0,
    games_win INT NOT NULL DEFAULT 0,
    total_score INT NOT NULL DEFAULT 0,
    PRIMARY KEY(id),
    FOREIGN KEY(user_id) REFERENCES Users(user_id)
);
