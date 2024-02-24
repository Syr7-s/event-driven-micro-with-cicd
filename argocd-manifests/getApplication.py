import requests
from concurrent.futures import ThreadPoolExecutor

def sync_application(argocd_server, token, application_name):
    url = f"{argocd_server}/api/v1/applications/{application_name}/sync"
    headers = {"Authorization": f"Bearer {token}"}

    response = requests.post(url, headers=headers, verify=False)

    if response.status_code == 200:
        print(f"Sync successful for application: {application_name}")
    else:
        print(f"Failed to sync application {application_name}. Status code: {response.status_code}")

def main():
    # Replace these values with your ArgoCD server URL and authentication token
    argocd_server_url = "https://argocd-server-url"
    auth_token = "your-auth-token"

    # List of applications to sync
    applications_to_sync = ["app1", "app2", "app3"]  # Add your application names

    with ThreadPoolExecutor() as executor:
        # Use submit to asynchronously execute the sync_application function for each application
        futures = [executor.submit(sync_application, argocd_server_url, auth_token, app) for app in applications_to_sync]

        # Wait for all tasks to complete
        for future in futures:
            future.result()

if __name__ == "__main__":
    main()


