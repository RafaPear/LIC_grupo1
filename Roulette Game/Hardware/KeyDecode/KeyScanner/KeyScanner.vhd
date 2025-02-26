library ieee;
use ieee.std_logic_1164.all;

entity KeyScanner is
    port(
        CLK: in std_logic;
        RESET: in std_logic;
        Kscan: in std_logic;
        Colum, ln: in std_logic_vector(3 downto 0);
        K: out std_logic_vector(3 downto 0);
        Kpress: out std_logic
    );

    architecture arch_KeyScanner of arch_KeyScanner is
        
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
                A : in std_logic_vector(1 downto 0);
                D : out std_logic_vector(3 downto 0)
            );
        end component;

        component PENC is
            port (
                I: in std_logic_vector (3 downto 0);
                Y: out std_logic_vector (3 downto 0);
                GS: out std_logic
            );
        end component;

        signal temp_colum, not_ln: std_logic_vector(3 downto 0);
        signal Q,Y: std_logic_vector(1 downto 0);
        
    
    begin
        not_ln <= not ln;
       
        Counter_inst: Counter port map(
            RESET => RESET,
            CE => Kscan,
            CLK => CLK,
            Q => Q
        );

        REG2_inst: REG2 port map(
            D => Y,
            RESET => RESET,
            SET => '1',
            EN => '1',
            CLK => Kscan,
            Q(0) => K(0),
            Q(1) => K(1),
        );

        Decoder_inst: Decoder port map(
            A => Q,
            D => temp_colum
        );

        PENC_inst: PENC port map(
            I => not_ln,
            Y => Y,
            GS => Kpress
        );

        Column <= not temp_colum;
    
    end arch ;

end KeyScanner;