library ieee;
use ieee.std_logic_1164.all;

entity RouletteGame is
    port(
        CLK: in std_logic;
        RESET: in std_logic;
        LCDsel: in std_logic;
        SCLK: in std_logic;
        SDX: in std_logic;
        E: out std_logic;
        D: out std_logic_vector(4 downto 0)
    );
end RouletteGame;

architecture arch_RouletteGame of RouletteGame is
    component UsbPort is
        port(
            inputPort : in std_logic_vector(7 downto 0);
            outputPort : out std_logic_vector(7 downto 0)
        );
    end component;

    component SLCDC is
        port(
        CLK: in std_logic;
        RESET: in std_logic;
		LCDsel: in std_logic;
        SCLK: in std_logic;
        SDX: in std_logic;
        E: out std_logic;
        D: out std_logic_vector (4 downto 0)
        );
    end component;

    component SLCDC is
        port(
            clk: in std_logic;
            RESET: in std_logic;
            LCDsel: in std_logic;
            SCLK: in std_logic;
            SDX: in std_logic;
            E: out std_logic;
            Dout: out std_logic_vector(4 downto 0)
        );
    end component;

    signal temp_Kval: std_logic;
    signal temp_K: std_logic_vector(3 downto 0);
    signal temp_inPort: std_logic_vector(7 downto 0);
    signal temp_outPort: std_logic_vector(7 downto 0);

begin
	 
signal temp_LCDsel, temp_SCLK, temp_SDX: std_logic;
signal temp_inPort: std_logic_vector(7 downto 0);
signal temp_outPort: std_logic_vector(7 downto 0);
 
 begin

	USLCDC: SLCDC port map(
		CLK => CLK,
		RESET => RESET,
		LCDsel => temp_LCDsel,
		SCLK => temp_SCLK,
		SDX => temp_SDX,
		E => E,
		D => D
	);
    UsbPort_inst: UsbPort port map(
        inputPort => temp_inPort,
        outputPort => temp_outPort
    );


    temp_LCDsel <= temp_outPort(0);
    temp_SCLK <= temp_outPort(1);
    temp_SDX <= temp_outPort(2);
	
end arch_RouletteGame;