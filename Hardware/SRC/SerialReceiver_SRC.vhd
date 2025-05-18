library ieee;
use ieee.std_logic_1164.all;

entity SRC is
    port(
        SDX: in std_logic;
        SCLK: in std_logic;
        SS: in std_logic;
        accept: in std_logic;
        RESET: in std_logic;
        clk_control: in std_logic;
        D: out std_logic_vector(7 downto 0);
        DX_val: out std_logic
    );
end SRC;

architecture structural of SRC is

    component ShiftRegister_SRC is
        port(
            CLK: in std_logic;
            RESET: in std_logic;
            D: in std_logic;
            EN: in std_logic;
            Q: out std_logic_vector(7 downto 0)
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

    component SerialControl_SRC is
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

    component ParityCheck_SRC is
        port(
            clk: in std_logic;
            init: in std_logic;
            data_in: in std_logic;
            error: out std_logic
        );
    end component;

    signal temp_wr: std_logic;
    signal temp_init: std_logic;
    signal temp_err: std_logic;
    signal temp_pFlag: std_logic;
    signal temp_dFlag: std_logic;
    signal counter_out: std_logic_vector(3 downto 0);

begin
    ShiftReg: ShiftRegister_SRC port map(
        CLK => SCLK,
        RESET => RESET,
        D => SDX,
        EN => temp_wr,
        Q => D
    );

    Counter: Counter4 port map(
        RESET => RESET,
        clr => temp_init,
        CLK => SCLK,
        Q => counter_out
    );

    --7
    temp_dFlag <= (not counter_out(3) and counter_out(2) and counter_out(1) and counter_out(0));

    --8
    temp_pFlag <= (counter_out(3) and not counter_out(2) and not counter_out(1) and not counter_out(0));

    ParityChecker: ParityCheck port map(
        clk => SCLK,
        init => temp_init,
        data_in => SDX,
        error => temp_err
    );

    SerialControl: SerialControl port map(
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

end structural;