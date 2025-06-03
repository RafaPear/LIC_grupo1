library ieee;
use ieee.std_logic_1164.all;

entity KeyScan is
    port(
        CLK: in std_logic;
        RESET: in std_logic;
        Kscan: in std_logic;
        LIN: in std_logic_vector(3 downto 0);
        COL: out std_logic_vector(3 downto 0);
        Kpress: out std_logic;
        K: out std_logic_vector(3 downto 0)
    );
end KeyScan;

architecture arch_KeyScan of KeyScan is
    
    component Counter is
        port(
            RESET: in std_logic;
            CE: in std_logic;
            CLK: in std_logic;
            Q: out std_logic_vector(3 downto 0)
        );
    end component;
    
    component Decoder is
        port(
            S: in std_logic_vector(1 downto 0);
            D: out std_logic_vector(3 downto 0)
        );
    end component;

    component MUX4x1 is
        port(
            A, B, C, D: in std_logic;
            S: in std_logic_vector(1 downto 0);
            O: out std_logic
        );
    end component;

    signal temp_COL: std_logic_vector(3 downto 0);
    signal temp_Q: std_logic_vector(3 downto 0);
    signal not_clk, temp_keyPress: std_logic;
begin
    Counter_inst: Counter port map(
        RESET => RESET,
        CE => Kscan,
        CLK => clk,
        Q => temp_Q
    );

    Decoder_inst: Decoder port map(
        S => temp_Q(3 downto 2),
        D => temp_COL
    );

    MUX4x1_inst: MUX4x1 port map(
        A => LIN(0),
        B => LIN(1),
        C => LIN(2),
        D => LIN(3),
        S => temp_Q(1 downto 0),
        O => temp_keyPress
    );

    kpress <= not temp_keyPress;
    K <= temp_Q;
    COL <= not temp_COL;
end arch_KeyScan;