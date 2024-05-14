from influxdb import InfluxDBClient
from core.config import get_setting

class InfluxDBConnection:
    def __init__(self, username: str, password: str, token: str):
        self.username = username
        self.password = password
        self.token = token
        self.client = None

    def connect(self):
        """Instantiate a connection to the InfluxDB."""
        self.client = InfluxDBClient(username=self.username, password=self.password, headers={"Authorization": self.token})
        print("Use authorization token: " + self.token)
        version = self.client.ping()
        print("Successfully connected to InfluxDB: " + version)

    def get_client(self) -> InfluxDBClient:
        """Return the InfluxDB client instance."""
        return self.client