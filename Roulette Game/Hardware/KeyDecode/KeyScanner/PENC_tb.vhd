LIBRARY ieee;
USE ieee.std_logic_1164.ALL;

ENTITY PENC_tb IS
END PENC_tb;

ARCHITECTURE test OF PENC_tb IS
    COMPONENT PENC
        PORT (
            I : IN STD_LOGIC_VECTOR (3 DOWNTO 0);
            Y : OUT STD_LOGIC_VECTOR (1 DOWNTO 0);
            GS : OUT STD_LOGIC
        );
    END COMPONENT;

    CONSTANT MCLK_PERIOD : TIME := 20 ns;
    CONSTANT MCLK_HALF_PERIOD : TIME := MCLK_PERIOD / 2;

    SIGNAL I_TB : STD_LOGIC_VECTOR (3 DOWNTO 0);
    SIGNAL Y_TB : STD_LOGIC_VECTOR (1 DOWNTO 0);
    SIGNAL GS_TB : STD_LOGIC;

BEGIN

    UUT : PENC PORT MAP(
        I => I_TB,
        Y => Y_TB,
        GS => GS_TB
    );

    stimulus : PROCESS
    BEGIN
        I_TB <= "0000";
        WAIT FOR 10 ns;
        ASSERT (GS_TB = '0') REPORT "Error: GS_TB = 1" SEVERITY error;

        WAIT FOR MCLK_HALF_PERIOD * 3;
        I_TB <= "0001";
        WAIT FOR 10 ns;
        ASSERT (Y_TB = "00") REPORT "Error: Y_TB /= 00" SEVERITY error;
        ASSERT (GS_TB = '1') REPORT "Error: GS_TB = 0" SEVERITY error;

        WAIT FOR MCLK_HALF_PERIOD * 3;
        I_TB <= "0010";
        WAIT FOR 10 ns;
        ASSERT (Y_TB = "01") REPORT "Error: Y_TB /= 01" SEVERITY error;
        ASSERT (GS_TB = '1') REPORT "Error: GS_TB = 0" SEVERITY error;

        WAIT FOR MCLK_HALF_PERIOD * 3;
        I_TB <= "0100";
        WAIT FOR 10 ns;
        ASSERT (Y_TB = "10") REPORT "Error: Y_TB /= 10" SEVERITY error;
        ASSERT (GS_TB = '1') REPORT "Error: GS_TB = 0" SEVERITY error;

        WAIT FOR MCLK_HALF_PERIOD * 3;
        I_TB <= "1000";
        WAIT FOR 10 ns;
        ASSERT (Y_TB = "11") REPORT "Error: Y_TB /= 11" SEVERITY error;
        ASSERT (GS_TB = '1') REPORT "Error: GS_TB = 0" SEVERITY error;

        WAIT FOR MCLK_HALF_PERIOD * 3;
        I_TB <= "1001";
        WAIT FOR 10 ns;
        ASSERT (Y_TB = "11") REPORT "Error: Y_TB /= 11" SEVERITY error;
        ASSERT (GS_TB = '1') REPORT "Error: GS_TB = 0" SEVERITY error;

        WAIT FOR MCLK_HALF_PERIOD * 3;
        I_TB <= "1010";
        WAIT FOR 10 ns;
        ASSERT (Y_TB = "11") REPORT "Error: Y_TB /= 11" SEVERITY error;
        ASSERT (GS_TB = '1') REPORT "Error: GS_TB = 0" SEVERITY error;

        WAIT FOR MCLK_HALF_PERIOD * 3;
        I_TB <= "1100";
        WAIT FOR 10 ns;
        ASSERT (Y_TB = "11") REPORT "Error: Y_TB /= 11" SEVERITY error;
        ASSERT (GS_TB = '1') REPORT "Error: GS_TB = 0" SEVERITY error;

        WAIT FOR MCLK_HALF_PERIOD * 3;
        I_TB <= "0101";
        WAIT FOR 10 ns;
        ASSERT (Y_TB = "10") REPORT "Error: Y_TB /= 10" SEVERITY error;
        ASSERT (GS_TB = '1') REPORT "Error: GS_TB = 0" SEVERITY error;

        WAIT FOR MCLK_HALF_PERIOD * 3;
        I_TB <= "0110";
        WAIT FOR 10 ns;
        ASSERT (Y_TB = "10") REPORT "Error: Y_TB /= 10" SEVERITY error;
        ASSERT (GS_TB = '1') REPORT "Error: GS_TB = 0" SEVERITY error;

        WAIT FOR MCLK_HALF_PERIOD * 3;
        I_TB <= "0111";
        WAIT FOR 10 ns;
        ASSERT (Y_TB = "10") REPORT "Error: Y_TB /= 10" SEVERITY error;
        ASSERT (GS_TB = '1') REPORT "Error: GS_TB = 0" SEVERITY error;

        WAIT FOR MCLK_HALF_PERIOD * 3;
        I_TB <= "0011";
        WAIT FOR 10 ns;
        ASSERT (Y_TB = "01") REPORT "Error: Y_TB /= 01" SEVERITY error;
        ASSERT (GS_TB = '1') REPORT "Error: GS_TB = 0" SEVERITY error;

        I_TB <= "0000";
        WAIT;
        REPORT "Testbench concluído sem erros" SEVERITY note;
    END PROCESS;
END test;
