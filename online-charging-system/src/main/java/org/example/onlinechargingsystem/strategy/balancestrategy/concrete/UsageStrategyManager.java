package org.example.onlinechargingsystem.strategy.balancestrategy.concrete;

import org.example.onlinechargingsystem.repository.ignite.IgniteSubscriberRepository;
import org.example.onlinechargingsystem.strategy.balancestrategy.abstrct.IUsageBalanceChecker;
import org.example.onlinechargingsystem.strategy.balancestrategy.abstrct.IUsageDeductionStrategy;
import org.example.onlinechargingsystem.strategy.balancestrategy.concrete.checker.UsageBalanceCheckerFactory;
import org.example.onlinechargingsystem.strategy.balancestrategy.concrete.deduction.UsageDeductionStrategyFactory;
import com.ramobeko.dgwtgf.model.UsageType;
import com.ramobeko.ignite.IgniteBalance;
import com.ramobeko.ignite.IgniteSubscriber;

public class UsageStrategyManager {

    /**
     * Verilen kullanım türüne göre mevcut bakiyeyi kontrol eder.
     *
     * @param usageType Kullanım türü (DATA, SMS, MINUTE)
     * @param balance   Kullanıcının bakiyesi
     * @param amount    Kullanılmak istenen miktar
     * @return Yeterli bakiye olup olmadığı (true / false)
     */
    public static boolean checkBalance(UsageType usageType, IgniteBalance balance, double amount) {
        IUsageBalanceChecker checker = UsageBalanceCheckerFactory.getChecker(usageType);
        return checker.getAvailableBalance(balance) >= amount;
    }

    /**
     * Verilen kullanım türüne göre bakiyeden düşme işlemi yapar.
     *
     * @param usageType               Kullanım türü (DATA, SMS, MINUTE)
     * @param subscriber              Kullanıcı bilgisi
     * @param amount                  Kullanılmak istenen miktar
     * @param igniteSubscriberRepository Kullanıcı bilgisini kaydedecek repository
     * @param subscNumber             Kullanıcı numarası
     */
    public static void deductBalance(
            UsageType usageType,
            IgniteSubscriber subscriber,
            int amount,
            IgniteSubscriberRepository igniteSubscriberRepository,
            Long subscNumber) {

        // Doğru stratejiyi seç
        IUsageDeductionStrategy deductionStrategy = UsageDeductionStrategyFactory.getStrategy(usageType);

        // Seçilen strateji ile bakiyeden düş
        deductionStrategy.deductBalance(subscriber, amount, igniteSubscriberRepository, subscNumber);
    }
}
