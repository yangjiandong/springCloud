package org.ssh.boot.notification.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableMap;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.ssh.boot.notification.domain.Frequency;
import org.ssh.boot.notification.domain.NotificationSettings;
import org.ssh.boot.notification.domain.NotificationType;
import org.ssh.boot.notification.domain.Recipient;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RecipientRepositoryTest {

    @Autowired
    private RecipientRepository repository;

    @Test
    public void shouldFindByAccountName() {

        NotificationSettings remind = new NotificationSettings();
        remind.setActive(true);
        remind.setFrequency(Frequency.WEEKLY);
        remind.setLastNotified(new Date(0));

        NotificationSettings backup = new NotificationSettings();
        backup.setActive(false);
        backup.setFrequency(Frequency.MONTHLY);
        backup.setLastNotified(new Date());

        Recipient recipient = new Recipient();
        recipient.setAccountName("test");
        recipient.setEmail("test@test.com");
        recipient.setScheduledNotifications(ImmutableMap.of(
            NotificationType.BACKUP, backup,
            NotificationType.REMIND, remind
        ));

        repository.save(recipient);

        Recipient found = repository.findByAccountName(recipient.getAccountName());
        assertEquals(recipient.getAccountName(), found.getAccountName());
        assertEquals(recipient.getEmail(), found.getEmail());

        assertEquals(recipient.getScheduledNotifications().get(NotificationType.BACKUP).getActive(),
            found.getScheduledNotifications().get(NotificationType.BACKUP).getActive());
        assertEquals(
            recipient.getScheduledNotifications().get(NotificationType.BACKUP).getFrequency(),
            found.getScheduledNotifications().get(NotificationType.BACKUP).getFrequency());
        assertEquals(
            recipient.getScheduledNotifications().get(NotificationType.BACKUP).getLastNotified(),
            found.getScheduledNotifications().get(NotificationType.BACKUP).getLastNotified());

        assertEquals(recipient.getScheduledNotifications().get(NotificationType.REMIND).getActive(),
            found.getScheduledNotifications().get(NotificationType.REMIND).getActive());
        assertEquals(
            recipient.getScheduledNotifications().get(NotificationType.REMIND).getFrequency(),
            found.getScheduledNotifications().get(NotificationType.REMIND).getFrequency());
        assertEquals(
            recipient.getScheduledNotifications().get(NotificationType.REMIND).getLastNotified(),
            found.getScheduledNotifications().get(NotificationType.REMIND).getLastNotified());
    }

    @Test
    public void shouldFindReadyForRemindWhenFrequencyIsWeeklyAndLastNotifiedWas8DaysAgo() {

        NotificationSettings remind = new NotificationSettings();
        remind.setActive(true);
        remind.setFrequency(Frequency.WEEKLY);
        remind.setLastNotified(DateUtils.addDays(new Date(), -8));

        Recipient recipient = new Recipient();
        recipient.setAccountName("test");
        recipient.setEmail("test@test.com");
        recipient.setScheduledNotifications(ImmutableMap.of(
            NotificationType.REMIND, remind
        ));

        repository.save(recipient);

        List<Recipient> found = repository.findReadyForRemind();
        assertFalse(found.isEmpty());
    }

    @Test
    public void shouldNotFindReadyForRemindWhenFrequencyIsWeeklyAndLastNotifiedWasYesterday() {

        NotificationSettings remind = new NotificationSettings();
        remind.setActive(true);
        remind.setFrequency(Frequency.WEEKLY);
        remind.setLastNotified(DateUtils.addDays(new Date(), -1));

        Recipient recipient = new Recipient();
        recipient.setAccountName("test");
        recipient.setEmail("test@test.com");
        recipient.setScheduledNotifications(ImmutableMap.of(
            NotificationType.REMIND, remind
        ));

        repository.save(recipient);

        List<Recipient> found = repository.findReadyForRemind();
        assertTrue(found.isEmpty());
    }

    @Test
    public void shouldNotFindReadyForRemindWhenNotificationIsNotActive() {

        NotificationSettings remind = new NotificationSettings();
        remind.setActive(false);
        remind.setFrequency(Frequency.WEEKLY);
        remind.setLastNotified(DateUtils.addDays(new Date(), -30));

        Recipient recipient = new Recipient();
        recipient.setAccountName("test");
        recipient.setEmail("test@test.com");
        recipient.setScheduledNotifications(ImmutableMap.of(
            NotificationType.REMIND, remind
        ));

        repository.save(recipient);

        List<Recipient> found = repository.findReadyForRemind();
        assertTrue(found.isEmpty());
    }

    @Test
    public void shouldNotFindReadyForBackupWhenFrequencyIsQuaterly() {

        NotificationSettings remind = new NotificationSettings();
        remind.setActive(true);
        remind.setFrequency(Frequency.QUARTERLY);
        remind.setLastNotified(DateUtils.addDays(new Date(), -91));

        Recipient recipient = new Recipient();
        recipient.setAccountName("test");
        recipient.setEmail("test@test.com");
        recipient.setScheduledNotifications(ImmutableMap.of(
            NotificationType.BACKUP, remind
        ));

        repository.save(recipient);

        List<Recipient> found = repository.findReadyForBackup();
        assertFalse(found.isEmpty());
    }
}
