mport tempfile
from wand.image import Image
import base64
import requests
import json
import os
from io import TextIOBase
import wikipedia
#import cloudstorage as gcs
#import time
from google.cloud import pubsub_v1
#from google.cloud import storage
from google.cloud import storage, vision
from itertools import (takewhile, repeat)

publisher = pubsub_v1.PublisherClient()
storage_client = storage.Client()

# project_id = os.environ["mms-project-333520"]
project_id = "mms-project-333520"

import sqlalchemy

# Set the following variables depending on your specific
# connection name and root password from the earlier steps:

# connection_name = "assignment-3-316707:europe-west4:assignment3"


# [START message_validatation_helper]
def validate_message(message, param):
 var = message.get(param)
 if not var:
  raise ValueError(
   "{} is not provided. Make sure you have \
        property {} in the request".format(
    param, param
   )
  )
 return var
# [END message_validatation_helper]


# [START functions_ocr_process]
def process(file, context):
 """Cloud Function triggered by Cloud Storage when a file is changed.
 Args:
  file (dict): Metadata of the changed file, provided by the triggering
         Cloud Storage event.
  context (google.cloud.functions.Context): Metadata of triggering event.
 Returns:
  None; the output is written to stdout and Stackdriver Logging
 """
 
   
 bucket = validate_message(file, "bucket")
 name = validate_message(file, "name")
 # total = percentage(bucket,name, linesize)
 if len(name.split('.')) == 1:
  plant_identify(bucket, name)
 elif name.split('.')[1] != 'json':
  plant_identify(bucket, name)
 else:
  pass
 
 # create_result_file(name)
 # splitfile(bucket, name, linesize, total)

 print("File {} processed.".format(file["name"]))


# [END functions_ocr_process]
def plant_identify(bucket,file):

 data_bucket = storage_client.get_bucket(bucket)
 data_blob = data_bucket.blob(file)
 data_stream = data_blob.open('rb')
 images = [base64.b64encode(data_stream.read()).decode("ascii")]

 your_api_key = "zx97zKZiFeW1PED8Yb85NNiqnsakdm7802rgjkgTZL5eMFYjoV"
 json_data = {
  "images": images,
  "modifiers": ["similar_images"],
  "plant_details": ["common_names", "url", "wiki_description", "taxonomy"]
 }

 response = requests.post(
  "https://api.plant.id/v2/identify",
  json=json_data,
  headers={
   "Content-Type": "application/json",
   "Api-Key": your_api_key
  }).json()

 # for suggestion in response["suggestions"]:
 #  print(suggestion["plant_name"])    # Taraxacum officinale
 #  print(suggestion["plant_details"]["common_names"])    # ["Dandelion"]
 #  print(suggestion["plant_details"]["url"])    # https://en.wikipedia.org/wiki/Taraxacum_officinale


 suggestion = response["suggestions"][0]
 name = suggestion["plant_name"]
 summary = wikipedia.summary(name)
 #print(name)
 #print(summary)

 plant_list = [('name', name), ('discription', summary)]
 dic = dict(plant_list)
 print(dic)
 content = json.dumps(dic, indent=4, ensure_ascii=False)
 print(content)
      
 result_bucket = storage_client.get_bucket(bucket)
 result_name = file
 result_name = result_name.split('.')[0]
 result_filename = "{}.json".format(result_name)
 result_blob = result_bucket.blob(result_filename)
 result_stream = result_blob.open('w')
 content = str(content)
 result_stream.write(content)
 result_stream.close()