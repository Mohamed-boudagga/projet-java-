-- ============================================================
-- SkillQuest — Script SQL complet
-- Module : Gestion des Etudiants
-- Base de donnees : skillquest
-- ============================================================
-- ETAPES :
--   1. Ouvrir phpMyAdmin (http://localhost/phpmyadmin)
--   2. Cliquer sur "SQL" en haut
--   3. Copier-coller tout ce fichier et cliquer "Executer"
-- ============================================================

-- 1) Creer la base de donnees si elle n'existe pas
CREATE DATABASE IF NOT EXISTS `skillquest`
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

-- 2) Utiliser la base
USE `skillquest`;

-- 3) Supprimer la table si elle existe deja (pour reset propre)
DROP TABLE IF EXISTS `etudiant`;

-- 4) Creer la table etudiant
CREATE TABLE `etudiant` (
    `id`           INT          NOT NULL AUTO_INCREMENT,
    `nom`          VARCHAR(100) NOT NULL,
    `prenom`       VARCHAR(100) NOT NULL,
    `email`        VARCHAR(150) NOT NULL UNIQUE,
    `mot_de_passe` VARCHAR(255) NOT NULL,
    `niveau`       INT          NOT NULL DEFAULT 1,
    `points`       INT          NOT NULL DEFAULT 0,
    `est_mentor`   TINYINT(1)   NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5) Inserer des donnees de test
INSERT INTO `etudiant` (`nom`, `prenom`, `email`, `mot_de_passe`, `niveau`, `points`, `est_mentor`) VALUES
('Ben Ali',  'Ahmed',  'ahmed@esprit.tn',  'pass123', 1,   0, 0),
('Trabelsi', 'Sarra',  'sarra@esprit.tn',  'pass456', 2, 150, 0),
('Mansouri', 'Khalil', 'khalil@esprit.tn', 'pass789', 3, 500, 1);

-- 6) Verification
SELECT * FROM `etudiant`;

-- ============================================================
-- Fin du script — Base de donnees prete !
-- ============================================================
