library ieee;
use ieee.std_logic_1164.all;

entity SLCDC_RouletteTeste is
    port(
        CLK: in std_logic;
        RESET: in std_logic;
        L_E: out std_logic; -- SLCDC
        L_Dout: out std_logic_vector(4 downto 0) -- SLCDC
    );
end SLCDC_RouletteTeste;

architecture arch_SLCDC_RouletteTeste of SLCDC_RouletteTeste is
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

    signal temp_inputPort, temp_outputPort: std_logic_vector(7 downto 0);
    signal temp_LCDsel: std_logic;
    signal temp_SCLK: std_logic;
    signal temp_SDX: std_logic;
begin
    -- Instantiate UsbPort
    UsbPort_inst: UsbPort
        port map(
            inputPort => temp_inputPort,
            outputPort => temp_outputPort
        );

    -- Instantiate SLCDC
    SLCDC_inst: SLCDC
        port map(
            clk => CLK,
            RESET => RESET,
            LCDsel => temp_LCDsel,
            SCLK => temp_SCLK,
            SDX => temp_SDX,
            E => L_E,
            Dout => L_Dout
        );

    temp_LCDsel <= temp_outputPort(0);
    temp_SDX <= temp_outputPort(3);
    temp_SCLK <= temp_outputPort(4);
end arch_SLCDC_RouletteTeste;