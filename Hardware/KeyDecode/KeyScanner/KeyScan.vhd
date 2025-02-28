library ieee;
use ieee.std_logic_1164.all;

entity KeyScan is
    port(
        CLK: in std_logic;
        CLK2: in std_logic;
        RESET: in std_logic;
        LIN: in std_logic_vector(3 downto 0);
        COL: out std_logic_vector(3 downto 0);
        K: out std_logic_vector(3 downto 0)
    );
end KeyScan;

architecture arch_KeyScan of KeyScan is
    
    component Counter is
        port(
            RESET: in std_logic;
            CE: in std_logic;
            CLK: in std_logic;
            Q: out std_logic_vector(1 downto 0)
        );
    end component;
    
    component REG2 is
        port(	
            D: in std_logic_vector(1 downto 0);
            RESET: in std_logic;
            SET: in std_logic;
            EN: in std_logic;
            CLK: in std_logic;
            Q: out std_logic_vector(1 downto 0)
        );
    end component;
    
    component Decoder is
        port(
            S: in std_logic_vector(1 downto 0);
            D: out std_logic_vector(3 downto 0)
        );
    end component;

    component PulseSignal is
        port (
		    A: in std_logic;
            CLK: in std_logic;
		    S: out std_logic
	    );
    end component;

    component PENC is
        port (
            I: in std_logic_vector (3 downto 0);
            Y: out std_logic_vector (1 downto 0);
            GS: out std_logic
        );
    end component;

    signal temp_COL, not_LIN: std_logic_vector(3 downto 0);
    signal temp_Q,temp_Y: std_logic_vector(1 downto 0);

    signal temp_GS, temp_not_GS, temp_pulsed, temp_not_pulsed: std_logic;
    
begin
    not_LIN <= not LIN;
    temp_not_GS <= not temp_GS;
    temp_not_pulsed <= not temp_pulsed;
   
    Counter_inst: Counter port map(
        RESET => RESET,
        CE => temp_not_GS,
        CLK => CLK,
        Q => temp_Q
    );
    
    REG2_inst: REG2 port map(
        D => temp_Y,
        RESET => RESET,
        SET => '0',
        EN => '1',
        CLK => temp_not_pulsed,
        Q(0) => K(2),
        Q(1) => K(3)
    );

    Decoder_inst: Decoder port map(
        S => temp_Q,
        D => temp_COL
    );

    PulseSignal_inst: PulseSignal port map(
        A => temp_GS,
        CLK => CLK2,
        S => temp_pulsed
    );

    PENC_inst: PENC port map(
        I => not_LIN,
        Y => temp_Y,
        GS => temp_GS
    );

    K(0) <= temp_Q(0);
    K(1) <= temp_Q(1);
    COL <= not temp_COL;
end arch_KeyScan;