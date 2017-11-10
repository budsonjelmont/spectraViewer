public class theorystruct
{

    theorystruct(int charge1)
    {
        values_ = new double[1];
        names_ = new String[1];
        charge_ = new int[1];
        mod_ = new String[1];
    }

    public void addFragment(String label, int charge1, double mz, String mod1, int aapos1)
    {
        if(values_.length == 1)
        {
            values_[0] = mz;
            names_[0] = label;
            charge_[0] = charge1;
        } else
        {
            double add1[] = new double[values_.length + 1];
            String sadd[] = new String[names_.length + 1];
            int cadd[] = new int[charge_.length + 1];
            String modd[] = new String[mod_.length + 1];
            sadd[names_.length] = label;
            add1[values_.length] = mz;
            cadd[charge_.length] = charge1;
            modd[mod_.length] = mod1;
            vmsdb.arraycopy(sadd, 0, names_, 0, names_.length);
            vmsdb.arraycopy(add1, 0, values_, 0, values_.length);
            vmsdb.arraycopy(cadd, 0, charge_, 0, charge_.length);
            vmsdb.arraycopy(modd, 0, mod_, 0, mod_.length);
            names_ = sadd;
            values_ = add1;
            charge_ = cadd;
            mod_ = modd;
        }
    }

    public void print()
    {
        vmsdb.println("frags");
        if(names_[0] != null)
        {
            for(int i = 0; i < names_.length; i++)
            {
                vmsdb.println(names_[i]);
                vmsdb.println(values_[i]);
                vmsdb.println(charge_[i]);
                vmsdb.println(mod_[i]);
            }

        }
    }

    double values_[];
    String names_[];
    int charge_[];
    String mod_[];
}
