library ieee;
use ieee.std_logic_1164.all;

entity SerialReceiver8 is
    port(
        SDX: in std_logic;
        SCLK: in std_logic;
        clk_control: in std_logic;
        SS: in std_logic;
        accept: in std_logic;
        RESET: in std_logic;
        D: out std_logic_vector(7 downto 0);
        DX_val: out std_logic
    );
end SerialReceiver8;

architecture arc_serialreceiver8 of SerialReceiver8 is

component ShiftRegister8 is
    port(
        CLK: in std_logic;
        RESET: in std_logic;
        D: in std_logic;
        EN: in std_logic;
        Q: out std_logic_vector(7 downto 0)
    );
end component;

component SerialControl is
    port(
        clk: in std_logic;
        en_Rx: in std_logic;
        pFlag: in std_logic;
        dFlag: in std_logic;
        RX_error: in std_logic;
        accept: in std_logic;
        init: out std_logic;
        DX_val: out std_logic;
        wr: out std_logic
    );
end component;

component ParityCheck is
    port(
        clk: in std_logic;
        init: in std_logic;
        data_in: in std_logic;
        error: out std_logic
    );
end component;

component Counter4 is
    port(
        RESET: in std_logic;
        clr: in std_logic;
        CLK: in std_logic;
        Q: out std_logic_vector(3 downto 0)
    );
end component;

signal temp_wr: std_logic;
signal temp_init: std_logic;
signal temp_err: std_logic;
signal temp_pFlag: std_logic;
signal temp_dFlag: std_logic;
signal temp_Q_counter: std_logic_vector(3 downto 0);

begin

    shiftRegister1: ShiftRegister8 port map(
        CLK => SCLK,
        RESET => RESET,
        D => SDX,
        EN => temp_wr,
        Q => D
    );

    counter1: Counter4 port map(
        RESET => RESET,
        clr => temp_init,
        CLK => SCLK,
        Q => temp_Q_counter
    );

    -- 8 1000
    -- 9 1001

    temp_dFlag <= temp_Q_counter(3) and not temp_Q_counter(2) and not temp_Q_counter(1) and not temp_Q_counter(0);
    temp_pFlag <= temp_Q_counter(3) and not temp_Q_counter(2) and not temp_Q_counter(1) and temp_Q_counter(0);

    ParityCheck2: ParityCheck port map(
        clk => SCLK,
        init => temp_init,
        data_in => SDX,
        error => temp_err
    );

    SerialControl2: SerialControl port map(
        clk => clk_control,
        en_Rx => SS,
        pFlag => temp_pFlag,
        dFlag => temp_dFlag,
        RX_error => temp_err,
        accept => accept,
        init => temp_init,
        DX_val => DX_val,
        wr => temp_wr
    );
end arc_serialreceiver8;