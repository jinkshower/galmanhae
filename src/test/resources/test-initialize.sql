INSERT INTO place (id, latitude, longitude, weatherx, weathery, created_at, updated_at,
                   code, name)
VALUES (1, 37.511000033694984, 127.06006305282887, 62, 127, '2024-08-27 18:00:39.000000',
        '2024-08-27 18:00:39.000000', 'POI001', '강남 MICE 관광특구'),
       (2, 37.567310951178904, 127.0110225062225, 61, 128, '2024-08-27 18:00:39.000000',
        '2024-08-27 18:00:39.000000', 'POI002', '동대문 관광특구'),
       (3, 37.56414952509789, 126.98185095527941, 61, 128, '2024-08-27 18:00:39.000000',
        '2024-08-27 18:00:39.000000', 'POI003', '명동 관광특구'),
       (4, 37.53443789733159, 126.99437253061254, 61, 127, '2024-08-27 18:00:39.000000',
        '2024-08-27 18:00:39.000000', 'POI004', '이태원 관광특구'),
       (5, 37.51647937513823, 127.1152740133431, 63, 127, '2024-08-27 18:00:39.000000',
        '2024-08-27 18:00:39.000000', 'POI005', '잠실 관광특구'),
       (6, 37.57000154171814, 126.99736943604613, 61, 128, '2024-08-27 18:00:39.000000',
        '2024-08-27 18:00:39.000000', 'POI006', '종로·청계 관광특구'),
       (7, 37.55391867558625, 126.92127401787192, 60, 127, '2024-08-27 18:00:39.000000',
        '2024-08-27 18:00:39.000000', 'POI007', '홍대 관광특구'),
       (8, 37.57987614402731, 126.97676560384028, 61, 128, '2024-08-27 18:00:39.000000',
        '2024-08-27 18:00:39.000000', 'POI008', '경복궁'),
       (9, 37.57167838080251, 126.9771913995408, 61, 128, '2024-08-27 18:00:39.000000',
        '2024-08-27 18:00:39.000000', 'POI009', '광화문·덕수궁'),
       (10, 37.57058502820595, 126.98341114649618, 61, 128, '2024-08-27 18:00:39.000000',
        '2024-08-27 18:00:39.000000', 'POI010', '보신각');

INSERT INTO weather (temperature, raining_probability, created_at, updated_at, place_id)
VALUES (28.0, 0.0, '2024-08-27 18:17:30.000000', '2024-08-27 18:17:30.536729', 1),
       (29.0, 0.0, '2024-08-27 18:17:30.000000', '2024-08-27 18:17:30.536745', 2),
       (29.0, 0.0, '2024-08-27 18:17:30.000000', '2024-08-27 18:17:30.536752', 3),
       (28.0, 0.0, '2024-08-27 18:17:30.000000', '2024-08-27 18:17:30.536755', 4),
       (29.0, 0.0, '2024-08-27 18:17:30.000000', '2024-08-27 18:17:30.536759', 5),
       (29.0, 0.0, '2024-08-27 18:17:30.000000', '2024-08-27 18:17:30.536763', 6),
       (28.0, 0.0, '2024-08-27 18:17:30.000000', '2024-08-27 18:17:30.536768', 7),
       (28.0, 0.0, '2024-08-27 18:17:30.000000', '2024-08-27 18:17:30.536770', 8),
       (28.0, 0.0, '2024-08-27 18:17:30.000000', '2024-08-27 18:17:30.536773', 9),
       (28.0, 0.0, '2024-08-27 18:17:30.000000', '2024-08-27 18:17:30.536775', 10);

INSERT INTO congestion (current_people, created_at, place_id, updated_at,
                        congestion_indicator)
VALUES (20000, '2024-08-27 18:17:30.000000', 1, '2024-08-27 18:17:30.000000', '보통'),
       (22000, '2024-08-27 18:17:30.000000', 2, '2024-08-27 18:17:30.000000', '보통'),
       (60000, '2024-08-27 18:17:30.000000', 3, '2024-08-27 18:17:30.000000', '보통'),
       (9000, '2024-08-27 18:17:30.000000', 4, '2024-08-27 18:17:30.000000', '보통'),
       (1000, '2024-08-27 18:17:30.000000', 5, '2024-08-27 18:17:30.000000', '여유'),
       (12000, '2024-08-27 18:17:30.000000', 6, '2024-08-27 18:17:30.000000', '약간 붐빔'),
       (72000, '2024-08-27 18:17:30.000000', 7, '2024-08-27 18:17:30.000000', '약간 붐빔'),
       (26000, '2024-08-27 18:17:30.000000', 8, '2024-08-27 18:17:30.000000', '여유'),
       (20000, '2024-08-27 18:17:30.000000', 9, '2024-08-27 18:17:30.000000', '약간 붐빔'),
       (18000, '2024-08-27 18:17:30.000000', 10, '2024-08-27 18:17:30.000000', '약간 붐빔');
