library ieee;
use ieee.std_logic_1164.all;

entity OutputBuffer is
    port (
        clk: in std_logic;
        reset: in std_logic;
        Data_in: in std_logic_vector(3 downto 0); 
        Load: in std_logic;
        ACK: in std_logic;
        Wreg: in std_logic;
        OBfree: out std_logic;
        Dval: out std_logic;
        Q_out: out std_logic_vector(3 downto 0)
    );
end OutputBuffer;

architecture arc_OutBuffer of OutputBuffer is
    component BufferControl
        Port (
            clk: in std_logic;
            reset: in std_logic;
            Load: in std_logic;
            ACK: in std_logic;
            Wreg: in std_logic;
            OBfree: out std_logic;
            Dval: out std_logic
        );
    end component;
    
    signal reg_enable : std_logic;
    
begin
    UUT: BufferControl port map (
            clk => clk,
            reset => reset,
            Load => Load,
            ACK => ACK,
            Wreg => Wreg,
            OBfree => OBfree,
            Dval => Dval
        );
    
    process(clk, reset)
    begin
        if reset = '1' then
            Q_out <= "0000"; -- Q_out <= (others => '0');
        elsif rising_edge(clk) then
            if Load = '1' then
                Q_out <= D_in;
            end if;
        end if;
    end process;
    
end arc_OutBuffer;