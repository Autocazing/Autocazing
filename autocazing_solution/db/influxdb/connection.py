from influxdb import InfluxDBClient
from core.config import get_setting

class InfluxDBConnection:
    def __init__(self, host: str, port: int, username: str, password: str, token: str):
        self.host = host
        self.port = port
        self.username = username
        self.password = password
        self.token = token
        self.client = None

    def connect(self):
        """Instantiate a connection to the InfluxDB."""
        print("connect to influxdb start")
        self.client = InfluxDBClient(host=self.host, port=self.port, username=self.username, password=self.password, headers={"Authorization": self.token})
        version = self.client.ping()
        print("Successfully connected to InfluxDB: " + version)

    def get_client(self) -> InfluxDBClient:
        """Return the InfluxDB client instance."""
        return self.client