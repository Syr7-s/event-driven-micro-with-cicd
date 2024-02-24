# Define the tables
tables = [
    {
        "name": "DevOpsValidations",
        "columns": [
            "ReleaseID",
            "RepositoryName",
            "ValidationStatus",
            "DeployReason",
            "StartDate",
            "EndDate",
            "Duration",
            "IsAutoCompDeployReason",
            "IsAutoCompValidation"
        ]
    },
    {
        "name": "ReleseStatus",
        "columns": [
            "ReleaseID",
            "RepositoryName",
            "JiraIssue",
            "DeployStatus",
            "DeployTime",
            "CommitterID",
            "UpdateTime"
        ]
    },
    {
        "name": "ReleaseDeployDuration",
        "columns": [
            "ReleaseID",
            "PackageName",
            "CommitterID",
            "AppTechnology",
            "TestDeployDuration",
            "TestDeployTime",
            "UatDeployDuration",
            "UatDeployTime",
            "PreliveDeployDuration",
            "PreliveDeployTime",
            "ProdDeployDuration",
            "ProdDeployTime",
            "ReleaseStartDate",
            "DeveloperTeamName",
            "CoreVersionComprasionUrl",
            "ReleaseType",
            "DirectorMail",
            "DepartmentMail",
            "JiraIssue",
            "AgileSquad"
        ]
    },
    {
        "name": "PlannedReleaseDayFreezeInfo",
        "columns": [
            "ReleaseID",
            "RepositoryName",
            "CommitterID",
            "DeployReason",
            "DirectorMail",
            "JiraIssueß",
            "NotificationMail",
            "ReadyForDeploy",
            "ReleaseStartDate",
            "ReleaseCurrentDate",
            "ProdDeployTime",
            "TeamName",
            "PackageTimeState"
        ]
    }
]

# Find the common columns
common_columns = set(tables[0]["columns"])
for table in tables[1:]:
    common_columns.intersection_update(table["columns"])

# Remove the common columns from each table
for table in tables:
    table["columns"] = [column for column in table["columns"] if column  in common_columns]

# Print the updated tables
for table in tables:
    print(f"{table['name']}:")
    for column in table["columns"]:
        print(f"    * {column}")
    print()