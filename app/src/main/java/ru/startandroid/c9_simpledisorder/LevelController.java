package ru.startandroid.c9_simpledisorder;

/**
 * Created by Vyacheslav on 02.05.2019.
 */

public class LevelController {

    public static String[][] GetLevelpack(int id_mode)
    {
        switch (id_mode) {
            case 0: return levels_normal;
            case 1: return levels_advanced;
            default: throw new NumberFormatException("WWwwwwwrrrrr");
        }
    }

    static String[][] levels_normal = {
            { "S4", "R31", "+-", "HB50", "АПорядок действий:\n1) Клик на знаке (+,-) выбирает нужное действие.\n2) Клик на числе совершает это действие с выбранным числом.\n\nЦель: превратить 0 в 31" },
            { "S4", "R41", "+-", "HB40", "АНу, раз уж ты справился с тем нелегким испытанием, вот тебе еще одно" },
            { "S4", "R59", "+-", "HB30" },
            { "S4", "R92", "+-", "StackSet*", "HB60", "АЕсли с помощью обычных методов не получается достичь желаемого, не помешает дополнительная помощь" },
            { "S4", "R6",  "+-", "StackSet/", "C92", "HB30", "АИногда необходимо идти в обратную сторону" },
            { "S4", "R1000", "+-*", "Overflow", "C6", "АНа этом уровне можно и преувеличить, не набирая ровно 1000" },
            { "S4", "R6000", "+-*", "Overflow", "C10", "HB35" },

            // 2
            { "S5", "K1", "R535", "+-*", "АНововведение! Теперь двойной клик на знаке сохраняет его" },
            { "S5", "K1", "R897", "+-*" },
            { "S5", "K1", "R932", "+-*" },
            { "S5", "K1", "R38", "C932", "+-*", "B40", "HB70", "D25_24_23_22_21_20_19", "StackSet/" },
            { "S5", "K2", "R4626", "HK0", "C38", "+-*" },
            { "S5", "K2", "R10000", "HR50000", "+-*", "Overflow" },
            { "S5", "K2", "R100000", "HR7000000", "+-*", "Overflow", "StackSet*", "HАА вот тут бы не помешало немного удачи" },

            // Gemini 3
            { "S4", "K3", "R400", "HR100_200_300_400", "HK0", "C-1", "G+", "StackSetG", "АПоявился специальный знак, дублирующий число, на которое нажали" },
            { "S4", "K3", "R4000", "HR-400_4000", "C-2", "G+-*", "StackSetG", "StackSetG", "StackSetG" },
            { "S5", "K3", "R17776", "HR400000", "C-3", "G+*" },
            { "S5", "K3", "R-4", "C500", "G+-" },
            { "S5", "K3", "R500", "C-5", "G+-*/" },
            { "S5", "K3", "R-6", "C800", "G+-*/" },
            { "S5", "K3", "R800000", "C-7", "HK0", "HR80000000", "LG_+_-3*_*_/",  "Overflow" },

            // 4
            { "S6", "K3", "R1200", "HR600_1200_1800", "L+2*_+10*_-", "HStackSetG", "HStackSetG" },
            { "S6", "K3", "R2400_2100", "HR900_600_1800_1500_2400", "L+2*_+10*_-3*", "StackSetG", "HStackSetG" },
            { "S6", "K3", "R3399", "HR1999_2998", "HB30", "P+10*_60_-_20_-3*_20", "StackSetG" },
            { "S4", "K3", "R199", "HR1_19_199", "HB25", "+*" },
            { "S4", "K3", "R1111", "HR4444", "+*" },
            { "S4", "K3", "R991", "HR9_91_991", "HB5", "+*" },
            { "S4", "K3", "R800", "HK0", "HR890", "L+10*_-3*", "StackSet*", "StackSetG",  "Overflow" },

            // Knight 5
            { "S5", "K2", "P*_60_/_40", "C1", "R900", "HR900_90000", "HD20", "АОсторожно! Целочисленное деление!" },
            { "S5", "K2", "P*_70_/_30", "C1", "R61600", "HR62600" },
            { "S5", "K2", "P*_60_/_40", "C4", "R29", "HR29_31", "StackSet/" },
            { "S5", "K2", "P*_60_/_40", "C2900", "R1", "HR1_71" },
            { "S5", "K2", "P*_60_/_40", "C2900", "R100", "HR100_113" },
            { "S5", "K2", "P*_60_/_40", "C2", "R113", "HR101_103" },
            { "S5", "K2", "P*_60_/_40", "C1", "R1000000", "HK0", "Overflow" },

            // Prince (Cancer 6)
            { "S4", "K2", "R8_80", "HR20_200", "+-C", "HStackSetG" },
            { "S5", "K2", "R-15_150", "HR150_-175_200", "+-C", "HStackSetG", "HStackSetG" },
            { "S4", "K2", "R1000", "HR4000", "+-", "StackSetC", "HStackSetG", "StackSet*" },
            { "S4", "HB0", "K2", "R3599", "L+_+2*", "HP+_20_+2*_80", "StackSetC", "StackSet*", "HD2_4_5_7_10_12_13_15" },
            { "S4", "K2", "R1110", "HR2222", "+*C" },
            { "S3", "K2", "R10000", "HR60000", "C1", "P*_50_C_50", "StackSet+", "StackSet+", "StackSet+", "StackSet+", "StackSet+" },
            { "S6", "K0", "R2056000", "HR123456789", "L+_+2*_+10*", "StackSetC", "StackSetC", "StackSetG", "StackSetG", "HStackSetG", "Overflow" },

            // Sorcerer (Monochrome 7)
            { "Sorcerer1", "S4", "R10", "HR13", "+*" },
            { "Sorcerer2", "S5", "R30", "HR10_30_90", "K2", "+*" },
            { "Sorcerer1", "S5", "R20", "HR7_-48", "L+2*_-", "StackSetC", "StackSet*" },
            { "Sorcerer2", "S5", "R30", "HR10_20_30_40", "+-", "StackSetG", "StackSetC" },
            { "Sorcerer2", "S5", "R55", "HR11_33_55_77", "+-*C", "StackSetG", "StackSetG", "StackSetG" },
            { "Sorcerer3", "S5", "R100", "L-_-3*", "StackSetG", "StackSetC", "StackSet*" },
            { "Sorcerer1", "S6", "R1000", "HR3600", "K3", "Overflow", "+*C" },

            // The Double (Light 8)
            { "Repeat1", "S6", "R99", "HR1_11_101", "+*C" },
            { "Repeat3", "S4", "R97", "K5", "*/C", "StackSet+", "StackSet+", "StackSet+", "StackSet+", "NStackSet+" },
            { "Repeat1", "S3", "R27", "+C", "HS4", "HR9_8_7", "H+*C", "NStackSet*", "NStackSet*" },
            { "Repeat4", "S5", "C1", "R2_3_4_5", "HR2_3_4_5_6_7_8", "K3", "+-*/" },
            { "Repeat2", "S6", "C10", "R10_10_10_10_10", "-*", "H+/" },
            { "Repeat1", "S5", "R2_7_1_8_2_8_1_8_2", "HS6", "HR2_7_1_8_2_8_1_8_2_8_4_5_9_0_4_5", "+C", "StackSetG", "StackSetC", "StackSet*" },
            { "Repeat1", "S6", "R1000", "HR3600", "K3", "Overflow", "+*C" },

            // Revolutionary (Pow 9)
            { "S5", "K0", "R618", "C1", "L+2*_-", "StackSetP" },
            { "S5", "K3", "R0_3398", "C618", "+-", "StackSetP", "NStackSetC", "NStackSetC", "HStackSetG" },
            { "S5", "K5", "R874989", "C3398", "L+_+10*_*_C", "StackSetP", "NStackSetG", "NStackSetG" },
            { "S6", "K3", "R4_84820", "C874989", "L+_*_/", "StackSetP", "StackSetG", "NStackSet*", "NStackSet/" },
            { "S6", "K5", "R45868_34365", "C84820", "+-*/", "StackSetP", "StackSetP", "StackSetP", "StackSetP", "StackSetP" },
            { "S6", "K5", "R63_81177_203_0_9", "C34365", "L+_-_G_C_P", "StackSet+", "StackSet-", "StackSetG", "StackSetC", "StackSetP" },
            { "S5", "K0", "*/", "D10", "C1", "R700000000", "HR7_37_700000000", "StackSetP", "StackSetP", "StackSetP" },

            // King (Special 10)
            { "S5", "K1", "*-", "C20", "R-2_-30_-400_-5000", "HR-30_-5000_-700000" },
            { "S4", "K2", "P*_60_/_40", "C1", "R21_31_41_51", "HC61", "HR51_41_31_21", "HStackSetG", "HStackSetG", "HStackSetG" },
            { "S3",       "*/", "C12", "R11", "HR11_10_9", "HStackSetG" },
            { "S3", "K0", "+GCP", "StackSetG", "StackSetG", "StackSetG", "C13118", "R27756", "HS7", "HK5", "HC0", "HR1_23_1050_13118_27756_124423_400000" },
            { "S3", "+", "StackSet*", "StackSet*", "R999", "HR999_1001" },
            { "S6", "HS3", "K4", "+-", "H+-G", "R2_3_5_7_9_11_13_17_19_23_27_29_31_37" },
            { "S3",       "Overflow", "+", "B0", "C1", "R10000", "StackSet*", "StackSetP", "StackSetC", "HR10000000", "HStackSetG" },

            /*
            // Revolutionary (Libra 8)
            { "S4", "R-60", "HR-120", "+L" },
            { "S4", "R110", "HR136", "+-L", "HP+_42_-_42_L_16", "HK1", "HB0" },
            { "S4", "K2", "P+_42_-_42_L_16", "HStackSetG", "HStackSetG", "HStackSetG", "R10_-20_30_-40", "HR10_-20_30_-40_50_-60_70" },

            { "S4", "K3", "P+_33_-_33_G_20_C_14", "R2_20_200_2000", "HS6", "HR2_201_20001_2000001", "HStackSetG" },
            { "S4", "R1000", "HR10000", "HB5", "+-C", "StackSet*", "StackSet*", "StackSet*" },
            { "S5", "R1000", "HR70000", "-GCL", "Overflow", "C6" },
            { "S5", "R6000", "+GCL", "C10", "HB50", "HR5000_700000" },
            //{ "S5", "K1", "*+", "C-40", "R4_30_200_1000", "HR6_50_400_3000_20000" },
            //{ "S5", "K2", "P*_70_/_30", "C1", "R899" },

            // The Double (Pow 8)
            { "S4", "K1", "R535", "StackSet^2", "L+2*_-" },
            { "S4", "K1", "R897", "StackSet^2", "L+2*_-" },
            { "S4", "K1", "R1932", "StackSet^2", "L+2*_+10*_-" },
            { "S4", "K1", "R8932", "C3", "StackSet^2", "L+2*_-3*"  },
            { "S4", "K2", "R4626", "C38", "StackSet^2", "L+2*_-3*" },
            { "S4", "K2", "+*", "R1000000", "StackSet^2" },
            { "S4", "K2", "+*G", "R50000000", "HR700000000", "StackSet^2", "Overflow" },

            // 177013 (Programming 11)
            { "Binary", "S4", "R14", "^", "D12", "HD2_10", "АБазовые навыки программирования еще никому не вредили" },
            { "Binary", "S4", "R13", "C14", "&^" },
            { "Binary", "S4", "R11_7", "C13", "&^⊕" },
            { "Binary", "S5", "R17_27", "C7", "&⊕" },
            { "Binary", "S5", "R18_28", "C27", "^⊕" },
            { "Binary", "S5", "R17_7_0_13", "C28", "⊕" },
            { "Binary", "S6", "R10_20_30_40_50", "C13", "⊕" },
*/
    };

    static String[][] levels_advanced = {
            // Special
            { "S5", "K2", "*-", "C20", "R-400" },
            { "S5", "K2", "*+", "C-40", "R200" },
            { "S3",       "*/", "C12", "R11" },
            { "S5", "K2", "P*_70_/_30", "C1", "R899" },
            { "S4", "K2", "P*_60_/_40", "R21_31_41_51", "C1" },
			{ "S6", "K4", "+-", "R2_3_5_7_9_11_13_17_19_23_27_29_31_37" },
            { "S3", "+", "StackSet*", "StackSet*", "R999", "HR999_1001" },

            // Pow 2
            { "S4", "K1", "R535", "StackSet^2", "L+2*_-" },
            { "S4", "K1", "R897", "StackSet^2", "L+2*_-" },
            { "S4", "K1", "R1932", "StackSet^2", "L+2*_-" },
            { "S4", "K1", "R8932", "C3", "StackSet^2", "L+2*_-3*"  },
            { "S4", "K2", "R4626", "C38", "StackSet^2", "L+2*_-3*" },
            { "S4", "K2", "+*", "R1000000", "StackSet^2" },
            { "S4", "K2", "+*", "R50000000", "HR700000000", "StackSet^2", "Overflow" },

            // Programming 3
            { "Binary", "S4", "R14", "C13", "L&_<<" },
            { "Binary", "S4", "R13", "C14", "L^_>>" },
            { "Binary", "S6", "K3", "R64_16_4_1", "L<<_>>_+1_-1_^" },
            { "Binary", "S4", "R0_7_0", "C15", "&⊕" },
            { "Binary", "S4", "R14_1_15", "C0", "^⊕" },
            { "Binary", "S6", "R63", "C15", "⊕" },
            { "Binary", "S6", "R", "C13", "⊕" },

            // Cancer 4
            { "S4", "K2", "R1000", "+-", "StackSetC", "StackSet*" },
            { "S4", "K2", "R3599", "L+_+2*", "StackSetC", "StackSet*" },
            { "S4", "K2", "R1110", "HR2222", "+*C" },
            { "S4", "K2", "R80", "HR120", "+-C" },
            { "S6", "K2", "R100_1", "HR100_19", "+-C" },
            { "S3", "K2", "R10000", "HR60000", "C1", "P*_50_C_50", "StackSet+", "StackSet+", "StackSet+", "StackSet+", "StackSet+" },
            { "S6", "K3", "R2000000", "*-C", "Overflow" },

            // Libra 5
            { "S4", "R-40", "HR-100", "+L" },
            { "S4", "R100", "HR136", "+-L", "HP+_42_-_42_L_16", "HK1", "HB0" },
            { "S4", "R1000", "HR10000", "HB5", "+-CL", "StackSet*", "StackSet*", "StackSet*" },
            { "S4", "K2", "HP+_42_-_42_L_16", "R10_-20_30_-40", "HR10_-20_30_-40_50_-60_70" },
            { "S4", "K3", "P+_38_-_38_G_12_C_12", "R2_20_200_2000", "HR2_201_20001_2000001" },
            { "S4", "R1000", "+GCL", "Overflow", "C6" },
            { "S4", "R6000", "+GCL", "Overflow", "C10", "HB35", "HR700000" },
    };
}
