-- INSERT INTO Wallet(id, address, balance) VALUES (nextval('hibernate_sequence'),'0x1','100');
-- INSERT INTO Wallet(id, address, balance) VALUES (nextval('hibernate_sequence'),'0x2','200');
-- INSERT INTO Wallet(id, address, balance) VALUES (nextval('hibernate_sequence'),'0x3','300');
-- INSERT INTO Wallet(id, address, balance) VALUES (nextval('hibernate_sequence'),'0x4','400');
-- INSERT INTO Wallet(id, address, balance) VALUES (nextval('hibernate_sequence'),'0x5','500');
-- INSERT INTO Wallet(id, address, balance) VALUES (nextval('hibernate_sequence'),'0x6','600');
-- INSERT INTO Wallet(id, address, balance) VALUES (nextval('hibernate_sequence'),'0x7','700');
-- INSERT INTO Wallet(id, address, balance) VALUES (nextval('hibernate_sequence'),'0x8','800');
-- INSERT INTO Wallet(id, address, balance) VALUES (nextval('hibernate_sequence'),'0x9','900');
CREATE TABLE IF NOT EXISTS `Person` (
    `id` INTEGER PRIMARY KEY AUTO_INCREMENT,
    `first_name` VARCHAR(50) NOT NULL,
    `age` INTEGER NOT NULL
);
INSERT INTO Wallet(
        id,
        address,
        ValueBalance,
        userId,
        privateKey,
        publicKey,
    )
VALUES (
        nextval('hibernate_sequence'),
        '0x1',
        '100',
        "10002",
        "asdkjsa",
        "kasdkjsh"
    );