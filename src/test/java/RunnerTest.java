import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotEquals;

public class RunnerTest {

    @Test
    public void should_pass_trim_cells() {
        String[] cells = {"cc ", "les ", "devs "};
        Runner.trimCells(cells);
        assertArrayEquals(new String[]{"cc", "les", "devs"}, cells);
    }

    @Test
    public void should_not_pass_trim_cells() {
        String[] cells = {"c c ", "l e s ", "d evs "};
        Runner.trimCells(cells);
        assertNotEquals(new String[]{"cc", "les", "devs"}, cells);
    }


}
