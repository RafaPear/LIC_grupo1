LIBRARY ieee;
USE ieee.std_logic_1164.ALL;

ENTITY PENC_tb IS
END PENC_tb;

ARCHITECTURE behavioral OF PENC_tb IS
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
    SIGNAL Y_tb : STD_LOGIC_VECTOR (1 DOWNTO 0);
    SIGNAL GS_tb : STD_LOGIC;

BEGIN

    UUT : PENC PORT MAP(
        I => I_TB,
        Y => Y_tb,
        GS => GS_tb
    );

    stimulus : PROCESS
    BEGIN
        I_TB <= "0000";
        WAIT FOR 10 ns;
        ASSERT (GS_tb = '0') REPORT "GS_tb should be equals to 1" SEVERITY failure;
        report "Test 1/12 Passed!" severity note;

        WAIT FOR MCLK_HALF_PERIOD * 3;
        I_TB <= "0001";
        WAIT FOR 10 ns;
        ASSERT (Y_tb = "00") REPORT "Y_tb should be diferent than 00" SEVERITY failure;
        ASSERT (GS_tb = '1') REPORT "GS_tb should be equals to 1" SEVERITY failure;
        report "Test 2/12 Passed!" severity note;

        WAIT FOR MCLK_HALF_PERIOD * 3;
        I_TB <= "0010";
        WAIT FOR 10 ns;
        ASSERT (Y_tb = "01") REPORT "Y_tb should be diferent than 01" SEVERITY failure;
        ASSERT (GS_tb = '1') REPORT "GS_tb should be equals to 1" SEVERITY failure;
        report "Test 3/12 Passed!" severity note;

        WAIT FOR MCLK_HALF_PERIOD * 3;
        I_TB <= "0100";
        WAIT FOR 10 ns;
        ASSERT (Y_tb = "10") REPORT "Y_tb should be diferent than 10" SEVERITY failure;
        ASSERT (GS_tb = '1') REPORT "GS_tb should be equals to 1" SEVERITY failure;
        report "Test 4/12 Passed!" severity note;

        WAIT FOR MCLK_HALF_PERIOD * 3;
        I_TB <= "1000";
        WAIT FOR 10 ns;
        ASSERT (Y_tb = "11") REPORT "Y_tb should be diferent than 11" SEVERITY failure;
        ASSERT (GS_tb = '1') REPORT "GS_tb should be equals to 1" SEVERITY failure;
        report "Test 5/12 Passed!" severity note;

        WAIT FOR MCLK_HALF_PERIOD * 3;
        I_TB <= "1001";
        WAIT FOR 10 ns;
        ASSERT (Y_tb = "11") REPORT "Y_tb should be diferent than 11" SEVERITY failure;
        ASSERT (GS_tb = '1') REPORT "GS_tb should be equals to 1" SEVERITY failure;
        report "Test 6/12 Passed!" severity note;

        WAIT FOR MCLK_HALF_PERIOD * 3;
        I_TB <= "1010";
        WAIT FOR 10 ns;
        ASSERT (Y_tb = "11") REPORT "Y_tb should be diferent than 11" SEVERITY failure;
        ASSERT (GS_tb = '1') REPORT " should be equals to 0" SEVERITY failure;
        report "Test 7/12 Passed!" severity note;

        WAIT FOR MCLK_HALF_PERIOD * 3;
        I_TB <= "1100";
        WAIT FOR 10 ns;
        ASSERT (Y_tb = "11") REPORT "Y_tb should be diferent than 11" SEVERITY failure;
        ASSERT (GS_tb = '1') REPORT "GS_tb should be equals to 1" SEVERITY failure;
        report "Test 8/12 Passed!" severity note;

        WAIT FOR MCLK_HALF_PERIOD * 3;
        I_TB <= "0101";
        WAIT FOR 10 ns;
        ASSERT (Y_tb = "10") REPORT "Y_tb should be diferent than 10" SEVERITY failure;
        ASSERT (GS_tb = '1') REPORT "GS_tbGS_tb should be equals to 1" SEVERITY failure;
        report "Test 9/12 Passed!" severity note;

        WAIT FOR MCLK_HALF_PERIOD * 3;
        I_TB <= "0110";
        WAIT FOR 10 ns;
        ASSERT (Y_tb = "10") REPORT "Y_tb should be diferent than 10" SEVERITY failure;
        ASSERT (GS_tb = '1') REPORT "GS_tbGS_tb should be equals to 1" SEVERITY failure;
        report "Test 10/12 Passed!" severity note;

        WAIT FOR MCLK_HALF_PERIOD * 3;
        I_TB <= "0111";
        WAIT FOR 10 ns;
        ASSERT (Y_tb = "10") REPORT "Y_tb should be diferent than 10" SEVERITY failure;
        ASSERT (GS_tb = '1') REPORT "GS_tbGS_tb should be equals to 1" SEVERITY failure;
        report "Test 11/12 Passed!" severity note;

        WAIT FOR MCLK_HALF_PERIOD * 3;
        I_TB <= "0011";
        WAIT FOR 10 ns;
        ASSERT (Y_tb = "01") REPORT "Y_tb should be diferent than 01" SEVERITY failure;
        ASSERT (GS_tb = '1') REPORT "GS_tbGS_tb should be equals to 1" SEVERITY failure;
        report "Test 12/12 Passed!" severity note;

        I_TB <= "0000";
        WAIT;
        report "UUT Penc passed all tests!" severity note;
    end process stimulus;
END ARCHITECTURE behavioral;
