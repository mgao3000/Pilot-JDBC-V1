package com.devmountain.training.jdbc;

import com.devmountain.training.dao.MajorDao;
import com.devmountain.training.model.MajorModel;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class MajorDaoJDBCTest {
    private Logger logger = LoggerFactory.getLogger(MajorDaoJDBCTest.class);

    private static MajorDao majorDao;

    private String tempMajorName = null;

    @BeforeClass
    public static void setupOnce() {
        majorDao = new MajorDaoJDBCImpl();
    }

    @AfterClass
    public static void teardownOnce() {
        majorDao = null;
    }

    @Before
    public void setup() {
        tempMajorName = "Major-12345";
    }

    @After
    public void teardown() {

    }

    @Test
    public void getMajorsTest() {
        List<MajorModel> majorModelList = majorDao.getMajors();
        int i = 1;
        for(MajorModel major : majorModelList) {
            logger.info("No.{} Major = {}", i, major);
            i++;
        }
    }

    @Test
    public void getMajorByIdTest() {
        /*
         * Pick up a random MajorModel from DB
         */
        MajorModel randomMajor = getRandomMajorModel();
        if(randomMajor == null) {
            logger.error("there is no major being found in database, please double check DB connection!");
        } else {
            Long majorId = randomMajor.getId();
            MajorModel retrievedMajorModel = majorDao.getMajorById(majorId);
            assertMajorModels(randomMajor, retrievedMajorModel);
        }
    }

    @Test
    public void getMajorByNameTest() {
        /*
         * Pick up a random MajorModel from DB
         */
        MajorModel randomMajor = getRandomMajorModel();
        if(randomMajor == null) {
            logger.error("there is no major being found in database, please double check DB connection!");
        } else {
            String majorName = randomMajor.getName();
            MajorModel retrievedMajorModel = majorDao.getMajorByName(majorName);
            assertMajorModels(randomMajor, retrievedMajorModel);
        }
    }

    @Test
    public void saveMajorTest() {
        MajorModel major = createMajorByName(tempMajorName);
        MajorModel savedMajor = majorDao.save(major);
        assertEquals(major.getName(), savedMajor.getName());
        assertEquals(major.getDescription(), savedMajor.getDescription());
        /*
         * Now clean up the saved Major from DB Major table
         */
        boolean deleteSuccessfulFlag = majorDao.delete(savedMajor);
        assertEquals(true, deleteSuccessfulFlag);
    }

    @Test
    public void deleteMajorTest() {
        MajorModel major = createMajorByName(tempMajorName);
        MajorModel savedMajor = majorDao.save(major);
        /*
         * Now delete the saved Major from DB Major table
         */
        boolean deleteSuccessfulFlag = majorDao.delete(savedMajor);
        assertEquals(true, deleteSuccessfulFlag);
    }

    @Test
    public void updateMajorTest() {
        MajorModel originalMajorModel = getRandomMajorModel();
        String currentMajorDesc = originalMajorModel.getDescription();
        String modifiedMajorDesc = currentMajorDesc + "---newUpdate";
        originalMajorModel.setDescription(modifiedMajorDesc);
        /*
         * Now start doing update operation
         */
        MajorModel updatedMajorModel = majorDao.update(originalMajorModel);
        assertMajorModels(originalMajorModel, updatedMajorModel);

        /*
         * now reset MajorModel description back to the original value
         */
        originalMajorModel.setDescription(currentMajorDesc);
        updatedMajorModel = majorDao.update(originalMajorModel);
        assertMajorModels(originalMajorModel, updatedMajorModel);
    }

    private void assertMajorModels(MajorModel randomMajor, MajorModel retrievedMajorModel) {
        assertEquals(randomMajor.getId(), retrievedMajorModel.getId());
        assertEquals(randomMajor.getName(), retrievedMajorModel.getName());
        assertEquals(randomMajor.getDescription(), retrievedMajorModel.getDescription());
    }

    private MajorModel createMajorByName(String name) {
        MajorModel major = new MajorModel();
        major.setName(name);
        major.setDescription(name + "--description");
        return major;
    }

    private MajorModel getRandomMajorModel() {
        List<MajorModel> majorModelList = majorDao.getMajors();
        MajorModel randomMajor = null;
        if(majorModelList != null && majorModelList.size() > 0) {
            int randomIndex = getRandomInt(0, majorModelList.size());
            randomMajor = majorModelList.get(randomIndex);
        }
        return randomMajor;
    }

    /**
     * This helper method return a random int value in a range between
     * min (inclusive) and max (exclusive)
     * @param min
     * @param max
     * @return
     */
    private int getRandomInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

}
