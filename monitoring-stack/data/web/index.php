<?php

require_once 'vendor/autoload.php';

use Elasticsearch\ClientBuilder;
use MongoDB\Client;

// Connect to Elasticsearch
$client = ClientBuilder::create()->setHosts([getenv('ELASTICSEARCH_HOST')])->build();
$params = [
    'index' => 'my_index',
    'body' => [
        'title' => 'Test Document',
        'body' => 'This is a test document.',
        'timestamp' => time(),
    ],
];
$response = $client->index($params);
print_r($response);

print_r('==================================');

// Connect to MongoDB
$mongo = new Client(getenv('MONGODB_URI'));
$collection = $mongo->test->articles;
$document = [
    'title' => 'Test Article',
    'body' => 'This is a test article.',
    'timestamp' => time(),
];
$result = $collection->insertOne($document);
print_r($result);

