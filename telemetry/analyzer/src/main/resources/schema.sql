DROP TABLE IF EXISTS scenario_actions;
DROP TABLE IF EXISTS scenario_conditions;
DROP TABLE IF EXISTS actions;
DROP TABLE IF EXISTS conditions;
DROP TABLE IF EXISTS sensors;
DROP TABLE IF EXISTS scenarios;

-- создаём таблицу scenarios
CREATE TABLE IF NOT EXISTS scenarios (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    hub_id VARCHAR(20),
    name VARCHAR(50),
    UNIQUE(hub_id, name)
);

-- создаём таблицу sensors
CREATE TABLE IF NOT EXISTS sensors (
    id VARCHAR(50) PRIMARY KEY,
    hub_id VARCHAR(20)
);

-- создаём таблицу conditions
-- в связи с тем, что value в avro бывает двух типов, разделил варианты на два поля
CREATE TABLE IF NOT EXISTS conditions (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    type VARCHAR(20),
    operation VARCHAR(20),
    value_int INTEGER,
    value_bool BOOLEAN
);

-- создаём таблицу actions
CREATE TABLE IF NOT EXISTS actions (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    type VARCHAR(20),
    value INTEGER
);

-- создаём таблицу scenario_conditions, связывающую сценарий, датчик и условие активации сценария
CREATE TABLE IF NOT EXISTS scenario_conditions (
    scenario_id BIGINT REFERENCES scenarios(id),
    sensor_id VARCHAR(50) REFERENCES sensors(id),
    condition_id BIGINT REFERENCES conditions(id),
    PRIMARY KEY (scenario_id, sensor_id, condition_id)
);

-- создаём таблицу scenario_actions, связывающую сценарий, датчик и действие, которое нужно выполнить при активации сценария
CREATE TABLE IF NOT EXISTS scenario_actions (
    scenario_id BIGINT REFERENCES scenarios(id),
    sensor_id VARCHAR(50) REFERENCES sensors(id),
    action_id BIGINT REFERENCES actions(id),
    PRIMARY KEY (scenario_id, sensor_id, action_id)
);

-- создаём функцию для проверки, что связываемые сценарий и датчик работают с одним и тем же хабом
CREATE OR REPLACE FUNCTION check_hub_id()
RETURNS TRIGGER AS
'
BEGIN
    IF (SELECT hub_id FROM scenarios WHERE id = NEW.scenario_id) != (SELECT hub_id FROM sensors WHERE id = NEW.sensor_id) THEN
        RAISE EXCEPTION ''Hub IDs do not match for scenario_id % and sensor_id %'', NEW.scenario_id, NEW.sensor_id;
    END IF;
    RETURN NEW;
END;
'
LANGUAGE plpgsql;

-- создаём триггер, проверяющий, что «условие» связывает корректные сценарий и датчик
CREATE OR REPLACE TRIGGER tr_bi_scenario_conditions_hub_id_check
BEFORE INSERT ON scenario_conditions
FOR EACH ROW
EXECUTE FUNCTION check_hub_id();

-- создаём триггер, проверяющий, что «действие» связывает корректные сценарий и датчик
CREATE OR REPLACE TRIGGER tr_bi_scenario_actions_hub_id_check
BEFORE INSERT ON scenario_actions
FOR EACH ROW
EXECUTE FUNCTION check_hub_id();