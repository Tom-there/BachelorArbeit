package de.hhu.cs.stups.algvis.plugins.TACtoCFG;

import de.hhu.cs.stups.algvis.data.ThreeAddressCodeInstruction;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
class TACtoCFGAlgoTest {

    @Test
    public void bigTest1(){
        TACtoCFGAlgo algo = new TACtoCFGAlgo("""
                t_1 = i + 1
                i = t_1
                t_2 = i * 8
                t_3 = a[t_2]
                if v goto 1
                t_4 = 3
                goto 4
                """);
        //Graph is empty
        assertThat(algo.getNodes(), hasSize(0));
        assertThat(algo.getEdges(), hasSize(0));
        //Code is formatted correctly todo
        assertThat(algo.getCode().size(), is(7));

        algo.step();
        assertThat(algo.getNodes(), hasSize(1));
        assertThat(algo.getEdges(), hasSize(0));
        assertThat(algo.getCode().size(), is(7));

        algo.step();
        assertThat(algo.getNodes(), hasSize(1));
        assertThat(algo.getEdges(), hasSize(0));
        assertThat(algo.getCode().size(), is(7));

        algo.step();
        assertThat(algo.getNodes(), hasSize(1));
        assertThat(algo.getEdges(), hasSize(0));
        assertThat(algo.getCode().size(), is(7));

        algo.step();
        assertThat(algo.getNodes(), hasSize(1));
        assertThat(algo.getEdges(), hasSize(0));
        assertThat(algo.getCode().size(), is(7));

        algo.step();
        assertThat(algo.getNodes(), hasSize(3));
        assertThat(algo.getEdges(), hasSize(0));
        assertThat(algo.getCode().size(), is(7));

        algo.step();
        assertThat(algo.getNodes(), hasSize(3));
        assertThat(algo.getEdges(), hasSize(0));
        assertThat(algo.getCode().size(), is(7));

        algo.step();
        assertThat(algo.getNodes(), hasSize(4));
        assertThat(algo.getEdges(), hasSize(0));
        assertThat(algo.getCode().size(), is(7));

        algo.step();
        assertThat(algo.getNodes(), hasSize(4));
        assertThat(algo.getEdges(), hasSize(0));
        assertThat(algo.getCode().size(), is(7));

        algo.step();
        assertThat(algo.getNodes(), hasSize(4));
        assertThat(algo.getEdges(), hasSize(1));
        assertThat(algo.getCode().size(), is(7));

        algo.step();
        assertThat(algo.getNodes(), hasSize(4));
        assertThat(algo.getEdges(), hasSize(1));
        assertThat(algo.getCode().size(), is(7));

        algo.step();
        assertThat(algo.getNodes(), hasSize(4));
        assertThat(algo.getEdges(), hasSize(1));
        assertThat(algo.getCode().size(), is(7));

        algo.step();
        assertThat(algo.getNodes(), hasSize(4));
        assertThat(algo.getEdges(), hasSize(2));
        assertThat(algo.getCode().size(), is(7));

        algo.step();
        assertThat(algo.getNodes(), hasSize(4));
        assertThat(algo.getEdges(), hasSize(4));
        assertThat(algo.getCode().size(), is(7));

        algo.step();
        assertThat(algo.getNodes(), hasSize(4));
        assertThat(algo.getEdges(), hasSize(4));
        assertThat(algo.getCode().size(), is(7));

        algo.step();
        assertThat(algo.getNodes(), hasSize(4));
        assertThat(algo.getEdges(), hasSize(5));
        assertThat(algo.getCode().size(), is(7));
    }
}