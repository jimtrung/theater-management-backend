INSERT INTO auditoriums (id, name, type, note, capacity) VALUES

-- 1
(gen_random_uuid(),
 'Phòng Chiếu 1',
 '2D',
 'Phòng tiêu chuẩn dành cho suất chiếu thường.',
 60),

-- 2
(gen_random_uuid(),
 'Phòng Chiếu 2',
 '3D',
 'Trang bị kính 3D và âm thanh vòm.',
 70),

-- 3
(gen_random_uuid(),
 'Phòng Chiếu 3',
 'IMAX',
 'Màn hình lớn, trải nghiệm cao cấp.',
 80),

-- 4
(gen_random_uuid(),
 'Phòng Chiếu 4',
 '4DX',
 'Ghế chuyển động và hiệu ứng đặc biệt.',
 55),

-- 5
(gen_random_uuid(),
 'Phòng Chiếu 5',
 '2D',
 'Phòng chiếu nhỏ, phù hợp suất chiếu ít khách.',
 50);
