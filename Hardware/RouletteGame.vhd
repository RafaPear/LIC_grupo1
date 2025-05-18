library ieee;
use ieee.std_logic_1164.all;

entity RouletteGame is
    port(
        CLK: in std_logic;
        ackti: in std_logic;
        RESET: in std_logic;
        send: in std_logic;
        InBit: in std_logic;
        lcdSel: in std_logic;
        activated: out std_logic;
        LCD_EN: out std_logic;
        LCD_D: out std_logic_vector(4 downto 0)
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
            clk: in std_logic;
            RESET: in std_logic;
            LCDsel: in std_logic;
            SCLK: in std_logic;
            SDX: in std_logic;
            E: out std_logic;
            Dout: out std_logic_vector(4 downto 0)
        );
    end component;

signal not_send: std_logic;
signal temp_LCDsel, temp_SCLK, temp_SDX: std_logic;
signal temp_inPort: std_logic_vector(7 downto 0);
signal temp_outPort: std_logic_vector(7 downto 0);

begin
    -- Invert the send signal
    not_send <= not send;

    -- Instantiate the SLCDC component

	USLCDC: SLCDC port map(
		CLK => CLK,
		RESET => RESET,
		LCDsel => temp_LCDsel,
		SCLK => temp_SCLK,
		SDX => temp_SDX,
		E => LCD_EN,
		Dout => LCD_D
	);

    activated <= ackti;

    UsbPort_inst: UsbPort port map(
        inputPort => temp_inPort,
        outputPort => temp_outPort
    );

    temp_LCDsel <= temp_inPort(0);
    temp_SCLK <= temp_inPort(1);
    temp_SDX <= temp_inPort(2);
end arch_RouletteGame;