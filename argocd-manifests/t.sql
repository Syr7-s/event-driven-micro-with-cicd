-- Create CombinedTable table
CREATE TABLE CombinedTable (
    ReleaseID INT,
    RepositoryName VARCHAR(255),
    ValidationStatus VARCHAR(255), /*ABORTED,COMPLETED,ROLLBACK,FAILED,NONE*/
    DeployReason VARCHAR(255), /*Planned,Unplanned,Fix,Major,Cezai-Mueyyide; Finansal-Zarar,Guvenlik-Problemi,Ozel-Surum,Pazar/Prestij-Kaybi,Surum-Dogrulama-Fixi,Yasal-Zorunluluk*/
    ValidationStartDate DATE,
    ValidationEndDate DATE,
    ValidationDuration INT,
    IsAutoCompDeployReason BOOLEAN,
    IsAutoCompValidation BOOLEAN,
    JiraIssue VARCHAR(255),
    ReleaseStatus VARCHAR(255),  /*IN_PROGRESS,FAILED,ABORTED,COMPLETED*/
    DeployTime DATETIME,
    CommitterID INT,
    AppTechnology VARCHAR(255),
    TestDeployDuration INT,
    TestDeployTime DATETIME,
    UatDeployDuration INT,
    UatDeployTime DATETIME,
    PreliveDeployDuration INT,
    PreliveDeployTime DATETIME,
    ProdDeployDuration INT,
    ProdDeployTime DATETIME,
    ReleaseStartDate DATE,
    DeveloperTeamName VARCHAR(255),
    CoreVersionComprasionUrl VARCHAR(255),
    --ReleaseType VARCHAR(255), ReleaseDeployDuration tablosunda Planned / UnPlanned bilgisi bu alanda true/false olarak tutulmuştur.
    IsPlanned /* True,False */
    DirectorMail VARCHAR(255),
    DepartmentMail VARCHAR(255),
    AgileSquad VARCHAR(255),
    NotificationMail VARCHAR(255),
    ReadyForDeploy BOOLEAN,
    ReleaseCurrentDate DATE,
    PackageTimeState VARCHAR(255)
    ReleaseType VARCHAR(255),/*Core,NonCore,DML,DDL,Rollback*/
    ReleaseDeployStateByEnv VARCHAR(255),/*Test,UAT,PreLive,Prod , Deployed,Failed, In Approval Approved*/
    /*Deploy Reason alanı yerine bu iki alan eklenecek*/
    ReleaseOutputType VARCHAR(255), /*Planned,UnPlanned*/
    ReleaseOutputReason VARCHAR(255), /*  Fix, Major, Sürüm Doğrulama Fixi, Cezai-Müeyyide; , Pazar/Prestij-Kaybi, Ozel-Surum, Finansal-Zarar, Guvenlik-Problemi, Yasal Zorunluluk */
);

/*

Bir paketin Fix ya da Major olarak belirlenmesi NonCore paketlerde DevOps-CoE.yml da pushToXLR task'ında girilen Jira Story veya IM kaydına göre belirlenir.
Core paketlerde ise bu bilgi template üzerinde  yer alan Get Jira Information task'ın da çalışan script ile belirlenir.

Bir paketin Planned ya da UnPlanned olarak belirlenmesi Non-Core paketlerde PROD faz'ın da Set Release Type taskında çalışan Planned/UnPlanned script sonucunda belirlenir. Core paketlerde PROD DEPLOY faz'ın da Set Release Type taskında çalışan Planned/UnPlanned script sonucunda belirlenir.

---
* 

Yeni süreçte bir paketin Planned/Unplanned olarak belirlenmesi Feed Freeze taskında çalışan scripte göre belirlenebilir.
*  Paketin PROD'a iletim tarihi Planli sürüm günü öncesi 05:00 - 23:00 arasında ise Planned olarak belirlenecek. 
*  Paketin PROD'a iletim tarihi Planli sürüm günü öncesi 05:00 - 23:00 saatleri arasında değil ise Unplanned olarak belirlenecek.

-- Paketin PROD ortama kurulumu için Direktor'e iletilen "Please Approve or Deny for Prod Deploy" mailinde Planned seçilir ise paket planned olarak belirlenir.

-- Sali gönderilen fakat kurulamadı Perşembe kurulacak ise Planned olarak belirlenecek.
-- Saat bileşen olarak girilmemeli

*/

/*DDL fibakod deploy duration bilgisinin alıması */