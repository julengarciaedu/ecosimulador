-- ============================================================
-- EcoSimulator - Script de carga de datos de prueba
-- ============================================================
-- Añade 10 hábitats y 26 especies adicionales, con valores
-- aproximados/ilustrativos (no verificados científicamente),
-- pensados para tener variedad de biomas, dietas y estados de
-- conservación con los que probar el simulador.
--
-- Uso:
--   mysql -u ecouserdb -p ecosimulator_db < seed_datos_demo.sql
--
-- Requiere que ya exista al menos un usuario con rol admin
-- (se usa para rellenar creado_por); si no lo encuentra, lo
-- deja en NULL.
-- ============================================================

SET @admin_id = (SELECT id FROM usuarios WHERE rol = 'admin' LIMIT 1);

-- ============================================================
-- HÁBITATS
-- ============================================================

INSERT INTO habitats
    (nombre, tipo_bioma, pais, region, latitud, longitud,
     altitud_min, altitud_max, area_total, temperatura_promedio,
     precipitacion_anual_promedio, humedad_promedio, ph_suelo_promedio,
     calidad_agua, biodiversidad_index, productividad_primaria,
     amenazas, medidas_conservacion, estatus_proteccion,
     descripcion, caracteristicas_unicas, imagen_portada,
     creado_por, fecha_monitoreo)
VALUES
    ('Selva Amazónica Central', 'bosque_tropical', 'Brasil', 'Amazonas',
     -3.4653, -62.2159, 30, 200, 550000.00, 26.5,
     2500, 88, 5.20,
     'buena', 0.920, 2200.00,
     'Deforestación, minería ilegal, incendios forestales',
     'Parque nacional, vigilancia satelital de deforestación',
     'parque_nacional',
     'Uno de los ecosistemas con mayor biodiversidad del planeta.',
     'Mayor selva tropical del mundo, cuenca hidrográfica más grande',
     NULL, @admin_id, '2024-05-10'),

    ('Sabana del Serengeti', 'sabana', 'Tanzania', 'Mara',
     -2.3333, 34.8333, 920, 1850, 14750.00, 25.0,
     900, 55, 6.50,
     'regular', 0.780, 900.00,
     'Caza furtiva, expansión agrícola, cambio climático',
     'Reserva de caza, patrullas anti-furtivismo',
     'reserva',
     'Sabana famosa por la gran migración anual de ñus y cebras.',
     'Mayor migración terrestre de mamíferos del mundo',
     NULL, @admin_id, '2024-03-22'),

    ('Bosque de Secuoyas de Sierra Nevada', 'bosque_templado', 'Estados Unidos', 'California',
     36.5786, -118.7651, 1200, 2500, 1630.00, 12.0,
     1500, 70, 5.80,
     'excelente', 0.650, 1400.00,
     'Incendios forestales de alta intensidad, sequías prolongadas',
     'Parque nacional, gestión controlada de incendios',
     'parque_nacional',
     'Hogar de algunos de los árboles más grandes y longevos del planeta.',
     'Secuoyas gigantes de más de 2000 años de antigüedad',
     NULL, @admin_id, '2024-07-02'),

    ('Taiga de Siberia Central', 'bosque_boreal', 'Rusia', 'Krasnoyarsk',
     60.7167, 92.6500, 100, 600, 890000.00, -5.0,
     450, 65, 4.90,
     'buena', 0.450, 600.00,
     'Explotación maderera, incendios, permafrost en deshielo',
     'Restricciones de tala en zonas protegidas',
     'sin_proteccion',
     'El mayor bosque boreal continuo del mundo.',
     'Bosque continuo más extenso del planeta',
     NULL, @admin_id, '2024-02-14'),

    ('Desierto del Sahara Central', 'desierto_calido', 'Argelia', 'Tamanrasset',
     23.0000, 5.5000, 200, 900, 920000.00, 35.0,
     25, 15, 7.80,
     'mala', 0.150, 50.00,
     'Sobrepastoreo puntual, avance de la desertificación',
     'Ninguna medida activa en la zona',
     'sin_proteccion',
     'El mayor desierto cálido del mundo, con extremos térmicos diarios muy amplios.',
     'Variación térmica diaria de más de 30°C',
     NULL, @admin_id, '2024-04-18'),

    ('Tundra Ártica de Svalbard', 'tundra_artica', 'Noruega', 'Svalbard',
     78.2232, 15.6267, 0, 400, 6100.00, -12.0,
     200, 75, 5.50,
     'excelente', 0.200, 150.00,
     'Retroceso del hielo marino, cambio climático acelerado',
     'Patrimonio protegido UNESCO, límites estrictos de acceso',
     'unesco',
     'Uno de los territorios habitados más septentrionales del planeta.',
     'Auroras boreales y sol de medianoche',
     NULL, @admin_id, '2024-01-30'),

    ('Humedal del Pantanal', 'humedal', 'Brasil', 'Mato Grosso',
     -16.9186, -56.6614, 100, 200, 150000.00, 27.0,
     1300, 82, 6.10,
     'buena', 0.850, 1600.00,
     'Incendios estacionales, contaminación agrícola aguas arriba',
     'Reserva de la biosfera, corredores ecológicos',
     'reserva',
     'El mayor humedal tropical del mundo.',
     'Mayor densidad de fauna observable de Sudamérica',
     NULL, @admin_id, '2024-06-05'),

    ('Manglares de Sundarbans', 'manglar', 'Bangladés', 'Khulna',
     21.9497, 89.1833, 0, 5, 10000.00, 26.0,
     1800, 85, 6.80,
     'regular', 0.700, 1100.00,
     'Aumento del nivel del mar, salinización, tala ilegal',
     'Patrimonio UNESCO, reserva de tigres',
     'unesco',
     'El mayor bosque de manglares contiguo del mundo.',
     'Único manglar con población residente de tigres de Bengala',
     NULL, @admin_id, '2024-05-27'),

    ('Gran Barrera de Coral', 'arrecife_coral', 'Australia', 'Queensland',
     -18.2871, 147.6992, 0, 0, 344400.00, 24.0,
     1200, 90, 8.10,
     'excelente', 0.950, 2500.00,
     'Blanqueamiento por calentamiento del agua, acidificación oceánica',
     'Parque marino protegido, restricciones de pesca',
     'parque_nacional',
     'El mayor sistema de arrecifes de coral del mundo, visible desde el espacio.',
     'Ecosistema marino con mayor biodiversidad conocida',
     NULL, @admin_id, '2024-08-11'),

    ('Pampas Templadas', 'pradera_templada', 'Argentina', 'Buenos Aires',
     -36.6167, -60.0000, 10, 250, 760000.00, 17.0,
     950, 60, 6.30,
     'buena', 0.550, 850.00,
     'Expansión agrícola intensiva, pérdida de pastizal nativo',
     'Protección parcial de reservas naturales privadas',
     'parcial',
     'Extensa llanura fértil, base de la ganadería y agricultura regional.',
     'Suelos entre los más fértiles del mundo',
     NULL, @admin_id, '2024-03-09');

-- ============================================================
-- ESPECIES
-- ============================================================

INSERT INTO especies
    (nombre_cientifico, nombre_comun, reino, familia, habitat_preferido,
     estado_conservacion, dieta, descripcion, caracteristicas,
     longevidad_promedio, tamano_promedio, temperatura_optima, humedad_optima,
     impacto_ecologico, tasa_reproduccion, es_endemica, es_invasora, es_protegida,
     imagen_url, creado_por, fuente_datos)
VALUES
    -- Selva Amazónica
    ('Panthera onca', 'Jaguar', 'Animalia', 'Felidae', 'Selva Amazónica',
     'NT', 'carnivoro', 'Mayor felino de América, depredador tope de la selva.', 'Mordida más fuerte de todos los felinos',
     15, 90.00, 26.0, 85.0, 0.80, 0.30, false, false, true,
     NULL, @admin_id, 'Datos de referencia aproximados (IUCN)'),

    ('Ara macao', 'Guacamayo escarlata', 'Animalia', 'Psittacidae', 'Selva Amazónica',
     'LC', 'herbivoro', 'Ave de gran tamaño y plumaje vistoso, frugívora.', 'Vuela hasta 56 km/h',
     50, 0.90, 26.0, 80.0, 0.30, 1.20, false, false, false,
     NULL, @admin_id, 'Datos de referencia aproximados (IUCN)'),

    ('Swietenia macrophylla', 'Caoba', 'Plantae', 'Meliaceae', 'Selva Amazónica',
     'VU', 'autotrofo', 'Árbol maderable de gran valor comercial, muy sobreexplotado.', 'Madera muy apreciada en ebanistería',
     300, 3500.00, 26.0, 85.0, 0.60, 0.05, false, false, true,
     NULL, @admin_id, 'Datos de referencia aproximados (IUCN)'),

    ('Atta cephalotes', 'Hormiga cortadora de hojas', 'Animalia', 'Formicidae', 'Selva Amazónica',
     'LC', 'herbivoro', 'Cultiva hongos a partir de fragmentos de hoja cortada.', 'Colonias de hasta 5 millones de individuos',
     7, 0.01, 26.0, 85.0, 0.40, 3.00, false, false, false,
     NULL, @admin_id, 'Datos de referencia aproximados'),

    -- Sabana del Serengeti
    ('Panthera leo', 'León africano', 'Animalia', 'Felidae', 'Sabana del Serengeti',
     'VU', 'carnivoro', 'Único felino verdaderamente social, vive en manadas.', 'Rugido audible a 8 km de distancia',
     14, 190.00, 25.0, 50.0, 0.90, 0.40, false, false, true,
     NULL, @admin_id, 'Datos de referencia aproximados (IUCN)'),

    ('Connochaetes taurinus', 'Ñu azul', 'Animalia', 'Bovidae', 'Sabana del Serengeti',
     'LC', 'herbivoro', 'Protagonista de la gran migración anual del Serengeti.', 'Migra más de 800 km al año',
     20, 180.00, 25.0, 55.0, 0.50, 0.80, false, false, false,
     NULL, @admin_id, 'Datos de referencia aproximados (IUCN)'),

    ('Acacia tortilis', 'Acacia paraguas', 'Plantae', 'Fabaceae', 'Sabana del Serengeti',
     'LC', 'autotrofo', 'Árbol característico de la sabana africana, tolerante a la sequía.', 'Copa aplanada característica',
     150, 800.00, 26.0, 40.0, 0.40, 0.20, false, false, false,
     NULL, @admin_id, 'Datos de referencia aproximados'),

    -- Sierra Nevada (secuoyas)
    ('Sequoiadendron giganteum', 'Secuoya gigante', 'Plantae', 'Cupressaceae', 'Bosque de Secuoyas',
     'EN', 'autotrofo', 'Uno de los árboles más grandes y longevos del mundo.', 'Puede superar los 80 metros de altura',
     2500, 500000.00, 12.0, 70.0, 0.70, 0.02, true, false, true,
     NULL, @admin_id, 'Datos de referencia aproximados (IUCN)'),

    ('Ursus americanus', 'Oso negro americano', 'Animalia', 'Ursidae', 'Bosque de Secuoyas',
     'LC', 'omnivoro', 'Oso de tamaño medio, muy adaptable en su dieta.', 'Excelente trepador de árboles',
     20, 120.00, 12.0, 65.0, 0.60, 0.30, false, false, false,
     NULL, @admin_id, 'Datos de referencia aproximados (IUCN)'),

    ('Strix occidentalis', 'Búho manchado del norte', 'Animalia', 'Strigidae', 'Bosque de Secuoyas',
     'NT', 'carnivoro', 'Ave rapaz nocturna, indicadora de bosques maduros.', 'Sensible a la fragmentación del hábitat',
     10, 0.60, 12.0, 70.0, 0.40, 0.50, false, false, true,
     NULL, @admin_id, 'Datos de referencia aproximados (IUCN)'),

    -- Taiga siberiana
    ('Panthera tigris altaica', 'Tigre siberiano', 'Animalia', 'Felidae', 'Taiga de Siberia',
     'EN', 'carnivoro', 'Mayor subespecie de tigre, adaptada al frío extremo.', 'Puede pesar hasta 300 kg',
     16, 220.00, -5.0, 65.0, 0.90, 0.20, false, false, true,
     NULL, @admin_id, 'Datos de referencia aproximados (IUCN)'),

    ('Rangifer tarandus', 'Reno', 'Animalia', 'Cervidae', 'Taiga de Siberia',
     'VU', 'herbivoro', 'Cérvido migratorio adaptado a climas fríos.', 'Único cérvido donde ambos sexos tienen cornamenta',
     15, 150.00, -5.0, 65.0, 0.40, 0.50, false, false, false,
     NULL, @admin_id, 'Datos de referencia aproximados (IUCN)'),

    ('Larix sibirica', 'Alerce siberiano', 'Plantae', 'Pinaceae', 'Taiga de Siberia',
     'LC', 'autotrofo', 'Conífera caducifolia dominante de la taiga siberiana.', 'Pierde las acículas en invierno, a diferencia de otras coníferas',
     400, 2000.00, -5.0, 60.0, 0.30, 0.10, false, false, false,
     NULL, @admin_id, 'Datos de referencia aproximados'),

    -- Desierto del Sahara
    ('Camelus dromedarius', 'Dromedario', 'Animalia', 'Camelidae', 'Desierto del Sahara',
     'LC', 'herbivoro', 'Mamífero adaptado a la escasez de agua y calor extremo.', 'Puede beber más de 100 litros de agua de una vez',
     40, 450.00, 35.0, 15.0, 0.30, 0.15, false, false, false,
     NULL, @admin_id, 'Datos de referencia aproximados'),

    ('Vulpes zerda', 'Zorro del desierto', 'Animalia', 'Canidae', 'Desierto del Sahara',
     'LC', 'carnivoro', 'El cánido más pequeño del mundo, orejas enormes para disipar calor.', 'Actividad nocturna para evitar el calor',
     12, 1.20, 35.0, 15.0, 0.30, 1.00, false, false, false,
     NULL, @admin_id, 'Datos de referencia aproximados (IUCN)'),

    ('Varanus griseus', 'Varano del desierto', 'Animalia', 'Varanidae', 'Desierto del Sahara',
     'LC', 'carnivoro', 'Gran lagarto depredador adaptado a ambientes áridos.', 'Puede correr a más de 30 km/h',
     15, 1.50, 35.0, 15.0, 0.35, 0.60, false, false, false,
     NULL, @admin_id, 'Datos de referencia aproximados'),

    -- Tundra ártica
    ('Ursus maritimus', 'Oso polar', 'Animalia', 'Ursidae', 'Tundra Ártica de Svalbard',
     'VU', 'carnivoro', 'Mayor depredador terrestre del Ártico, muy afectado por el deshielo.', 'Puede oler una foca a más de 1 km bajo el hielo',
     25, 450.00, -10.0, 75.0, 0.90, 0.15, false, false, true,
     NULL, @admin_id, 'Datos de referencia aproximados (IUCN)'),

    ('Lagopus muta', 'Perdiz nival', 'Animalia', 'Phasianidae', 'Tundra Ártica de Svalbard',
     'LC', 'herbivoro', 'Ave adaptada al frío extremo, cambia de plumaje según la estación.', 'Plumaje blanco en invierno para camuflaje',
     8, 0.50, -8.0, 75.0, 0.20, 1.50, false, false, false,
     NULL, @admin_id, 'Datos de referencia aproximados'),

    -- Pantanal
    ('Pteronura brasiliensis', 'Nutria gigante', 'Animalia', 'Mustelidae', 'Pantanal',
     'EN', 'carnivoro', 'Mayor mustélido del mundo, vive en grupos familiares.', 'Puede medir hasta 1,8 metros',
     12, 30.00, 27.0, 85.0, 0.60, 0.40, false, false, true,
     NULL, @admin_id, 'Datos de referencia aproximados (IUCN)'),

    ('Caiman yacare', 'Yacaré', 'Animalia', 'Alligatoridae', 'Pantanal',
     'LC', 'carnivoro', 'Caimán de tamaño medio muy abundante en el Pantanal.', 'Una de las mayores concentraciones de cocodrilianos del mundo',
     30, 60.00, 27.0, 85.0, 0.50, 0.50, false, false, false,
     NULL, @admin_id, 'Datos de referencia aproximados'),

    ('Lithobates catesbeianus', 'Rana toro americana', 'Animalia', 'Ranidae', 'Pantanal',
     'LC', 'carnivoro', 'Especie invasora en Sudamérica, desplaza a anfibios nativos.', 'Considerada una de las especies invasoras más dañinas del mundo',
     9, 0.50, 25.0, 80.0, 0.60, 2.50, false, true, false,
     NULL, @admin_id, 'Datos de referencia aproximados (UICN especies invasoras)'),

    -- Sundarbans
    ('Panthera tigris tigris', 'Tigre de Bengala', 'Animalia', 'Felidae', 'Manglares de Sundarbans',
     'EN', 'carnivoro', 'Única población de tigres adaptada a vivir en manglares.', 'Buen nadador, cruza canales de marea',
     15, 220.00, 26.0, 85.0, 0.85, 0.25, false, false, true,
     NULL, @admin_id, 'Datos de referencia aproximados (IUCN)'),

    ('Rhizophora mangle', 'Mangle rojo', 'Plantae', 'Rhizophoraceae', 'Manglares de Sundarbans',
     'LC', 'autotrofo', 'Especie fundacional del ecosistema de manglar, tolera agua salobre.', 'Raíces aéreas en forma de zanco',
     100, 400.00, 26.0, 90.0, 0.50, 0.30, false, false, false,
     NULL, @admin_id, 'Datos de referencia aproximados'),

    -- Gran Barrera de Coral
    ('Amphiprion ocellaris', 'Pez payaso', 'Animalia', 'Pomacentridae', 'Gran Barrera de Coral',
     'LC', 'omnivoro', 'Vive en simbiosis con anémonas marinas.', 'Todos nacen macho y pueden cambiar de sexo',
     10, 0.10, 24.0, 90.0, 0.20, 2.00, false, false, false,
     NULL, @admin_id, 'Datos de referencia aproximados'),

    ('Chelonia mydas', 'Tortuga verde marina', 'Animalia', 'Cheloniidae', 'Gran Barrera de Coral',
     'EN', 'herbivoro', 'Una de las tortugas marinas más grandes, se alimenta de algas y pastos marinos.', 'Puede vivir más de 80 años',
     80, 150.00, 24.0, 90.0, 0.40, 0.10, false, false, true,
     NULL, @admin_id, 'Datos de referencia aproximados (IUCN)'),

    -- Pampas
    ('Rhea americana', 'Ñandú', 'Animalia', 'Rheidae', 'Pampas Templadas',
     'NT', 'herbivoro', 'Mayor ave de Sudamérica, no voladora.', 'El macho incuba y cría a los polluelos',
     15, 25.00, 17.0, 60.0, 0.30, 0.60, false, false, false,
     NULL, @admin_id, 'Datos de referencia aproximados (IUCN)'),

    ('Ozotoceros bezoarticus', 'Venado de las pampas', 'Animalia', 'Cervidae', 'Pampas Templadas',
     'NT', 'herbivoro', 'Cérvido nativo de las llanuras sudamericanas, en fuerte declive.', 'Población reducida a menos del 1% de su rango histórico',
     10, 35.00, 17.0, 60.0, 0.35, 0.50, false, false, true,
     NULL, @admin_id, 'Datos de referencia aproximados (IUCN)'),

    -- Descomponedor genérico (no ligado a un único hábitat)
    ('Agaricus campestris', 'Champiñón silvestre', 'Fungi', 'Agaricaceae', 'Bosque',
     'LC', 'detritivoro', 'Hongo descomponedor de materia orgánica del suelo forestal.', 'Fundamental en el reciclaje de nutrientes del bosque',
     1, 0.05, 15.0, 70.0, 0.20, 5.00, false, false, false,
     NULL, @admin_id, 'Datos de referencia aproximados');
