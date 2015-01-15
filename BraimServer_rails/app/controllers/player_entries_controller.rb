class PlayerEntriesController < ApplicationController
  before_action :set_player_entry, only: [:show, :edit, :update, :destroy]

  respond_to :html

  def index
    @player_entries = PlayerEntry.all
    respond_with(@player_entries)
  end

  def show
    respond_with(@player_entry)
  end

  def new
    @player_entry = PlayerEntry.new
    respond_with(@player_entry)
  end

  def edit
  end

  def create
    @player_entry = PlayerEntry.new(player_entry_params)
    @player_entry.save
    respond_with(@player_entry)
  end

  def update
    @player_entry.update(player_entry_params)
    respond_with(@player_entry)
  end

  def destroy
    @player_entry.destroy
    respond_with(@player_entry)
  end

  private
    def set_player_entry
      @player_entry = PlayerEntry.find(params[:id])
    end

    def player_entry_params
      params[:player_entry]
    end
end
