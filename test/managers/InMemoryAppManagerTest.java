package managers;

import utils.Managers;

class InMemoryAppManagerTest extends AppManagerTest<InMemoryAppManager> {

    private InMemoryAppManagerTest() {
        super(Managers.getDefault());
    }
}