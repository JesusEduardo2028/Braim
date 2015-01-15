# This file is copied to spec/ when you run 'rails generate rspec:install'
ENV['RAILS_ENV'] ||= 'test'
require File.expand_path("../../config/environment", __FILE__)
require 'rspec/rails'
require 'capybara/rspec'
require 'database_cleaner'

Dir[Rails.root.join("spec/support/**/*.rb")].each { |f| require f }

# Configure Capybara
Capybara.default_host = "http://127.0.0.1"
Capybara.javascript_driver = :webkit
Capybara.register_driver :rack_test do |app|
  Capybara::RackTest::Driver.new(app, :headers => { 'HTTP_USER_AGENT' => 'Capybara' })
end

RSpec.configure do |config|

  # If true, the base class of anonymous controllers will be inferred
  # automatically. This will be the default behavior in future versions of
  # rspec-rails.
  config.infer_base_class_for_anonymous_controllers = false

  # Run specs in random order to surface order dependencies. If you find an
  # order dependency and want to debug it, you can fix the order by providing
  # the seed, which is printed after each run.
  #     --seed 1234
  config.order = 'random'

  # DatabaseCleaner config
  static_info_tables = %w[]

  config.before(:suite) do
    DatabaseCleaner.strategy = :truncation, {except: static_info_tables}
    DatabaseCleaner.start
    DatabaseCleaner.clean
  end

  config.before(:context) do |example|
    DatabaseCleaner.clean
  end

  config.before(:example) do |example|
    DatabaseCleaner.clean unless example.metadata[:keep_db]
  end

  # Exclude options
  config.filter_run_excluding :exclude => true

  # Factory Girl methods
  config.include FactoryGirl::Syntax::Methods

  # Include devise test helpers in controller specs
  config.include Devise::TestHelpers, :type => :controller

  # Include mongoid matches in model specs
  config.include Mongoid::Matchers, type: :model

  config.include Capybara::DSL

  config.include RSpec::Rails::RequestExampleGroup, type: :request, parent_example_group: { file_path: /spec\/api/ }

end
