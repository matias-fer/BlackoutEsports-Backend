-- Se ejecuta automáticamente al levantar la app.
-- Incluye al jugador de ejemplo que ya está hardcodeado en el frontend (data/players.ts)
-- para que al conectar la API los datos coincidan.

INSERT INTO players (
    id, handle, real_name, team, country_code, role, game,
    dpi, in_game_sens, edpi, windows_sens, hz, resolution, aspect_ratio,
    crosshair_code, crosshair_color, crosshair_size, crosshair_thickness, crosshair_gap,
    crosshair_outline, crosshair_opacity, crosshair_center_dot, crosshair_outer_lines,
    gear_mouse, gear_mousepad, gear_keyboard, gear_monitor, gear_headset,
    updated_at
) VALUES
('prinzcl', 'Prinzcl', 'Maximiliano', 'Blackout Esports', 'CL', 'Duelista', 'valorant',
 1600, 0.11, 176.0, 6, 360, '1920x1080', '16:9 Nativa',
 '0;c;1;s;1;P;o;1;f;0;0l;4;0v;4;0o;2;0a;1;0f;0;1b;0;S;o;1', '#00FF00', 4, 4, 2,
 true, 1.0, false, false,
 'Rodent RX Superlight', 'Glide XL', 'Tenkey Zero HE', 'Vantage 24.5" 360Hz', 'Aural Pro X',
 '2026-06-14'),

('nightowl', 'NightOwl', 'Fernanda Rojas', 'Blackout Esports', 'CL', 'Centinela', 'valorant',
 800, 0.35, 280.0, 6, 240, '1920x1080', '16:9 Nativa',
 '0;P;c;1;o;1', '#FFFFFF', 3, 3, 3,
 true, 1.0, true, false,
 'Featherlight Wireless', 'Control Pro', 'Compact 65%', 'Vantage 24.5" 360Hz', 'Aural Pro X',
 '2026-06-10'),

('kaidoscope', 'Kaidoscope', 'Ignacio Muñoz', 'Blackout Esports', 'CL', 'Iniciador', 'valorant',
 400, 0.42, 168.0, 6, 165, '2560x1440', '16:9 Nativa',
 '0;s;1;P;c;5;o;1', '#00FFFF', 2, 5, 1,
 false, 0.8, false, true,
 'Rodent RX Superlight', 'Glide XL', 'Tenkey Zero HE', 'Vantage 27" 165Hz', 'Cans Studio',
 '2026-06-05')
ON CONFLICT (id) DO NOTHING;
