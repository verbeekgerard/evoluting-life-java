package main;

public class Options {
//	define([
//	        'option',
//	        'options'
//	      ],
//	        function(Option, worldOptions, undefined) {
//	          "use strict";
//
//	          var options = {
//	            evolution: {
//	              /*
//	                  Mutations can be:
//	                  - replace a gene
//	                  - adding/removing a layer after removing a gene when the number of genes is lower than x
//	                  - adding/removing a gene
//	                  - modify a genes threshold
//	                  - modify a genes relaxation
//	                  - modify axon strength
//	              */
//	              mutationFraction: new Option(0.001), // the percentual delta of the change (up or down)
//
//	              reset: function() {
//	                this.mutationFraction.reset();
//	              }
//	            },
//
//	            genome: {
//	              sensorGeneReplacementRate: new Option(0.01),
//	              movementGeneReplacementRate: new Option(0.01),
//
//	              reset: function() {
//	                this.sensorGeneReplacementRate.reset();
//	                this.movementGeneReplacementRate.reset();
//	              }
//	            },
//
//	            life: {
//	              minOldAge: new Option(3000),
//	              maxOldAge: new Option(4000),
//	              minNutrition: new Option(1.0),
//	              maxNutrition: new Option(3.0),
//
//	              oldAgeMutationRate: new Option(0.03),
//	              nutritionMutationRate: new Option(0.03),
//
//	              reset: function() {
//	                this.minOldAge.reset();
//	                this.maxOldAge.reset();
//	                this.minNutrition.reset();
//	                this.maxNutrition.reset();
//
//	                this.oldAgeMutationRate.reset();
//	                this.nutritionMutationRate.reset();
//	              }
//	            },
//
//	            sensor: {
//	              minViewDistance: new Option(16 * 1),
//	              maxViewDistance: new Option(16 * 14),
//	              minFieldOfView: new Option(Math.PI / 32),
//	              maxFieldOfView: new Option(Math.PI),
//
//	              viewDistanceMutationRate: new Option(0.1),
//	              fieldOfViewMutationRate: new Option(0.1),
//
//	              reset: function() {
//	                this.minViewDistance.reset();
//	                this.maxViewDistance.reset();
//	                this.minFieldOfView.reset();
//	                this.maxFieldOfView.reset();
//
//	                this.viewDistanceMutationRate.reset();
//	                this.fieldOfViewMutationRate.reset();
//	              }
//	            },
//
//	            movement: (function() {
//	              // F = Vmax * friction
//	              var calculateForce = function(maxVelocity, friction) {
//	                return maxVelocity * friction;
//	              };
//
//	              var minA = calculateForce(0.5, worldOptions.physics.angularFriction.get());
//	              var maxA = calculateForce(5.0, worldOptions.physics.angularFriction.get());
//	              var minL = calculateForce(5.0, worldOptions.physics.linearFriction.get());
//	              var maxL = calculateForce(50.0, worldOptions.physics.linearFriction.get());
//
//	              return {
//	                minAngularForce: new Option(minA),
//	                maxAngularForce: new Option(maxA),
//	                minLinearForce: new Option(minL),
//	                maxLinearForce: new Option(maxL),
//
//	                angularForceMutationRate: new Option(0.15),
//	                linearForceMutationRate: new Option(0.15),
//
//	                reset: function() {
//	                  this.minAngularForce.reset();
//	                  this.maxAngularForce.reset();
//	                  this.minLinearForce.reset();
//	                  this.maxLinearForce.reset();
//
//	                  this.angularForceMutationRate.reset();
//	                  this.linearForceMutationRate.reset();
//	                }
//	              };
//	            })(),
//
//	            brain: {
//	              minHiddenLayers: new Option(1),
//	              maxHiddenLayers: new Option(5),
//	              maxNeuronsPerLayer: new Option(16),
//
//	              layerMutationRate: new Option(0.05),    // adding or removing a gene
//	              geneMutationRate: new Option(0.15),     // percentual chance of genes within a genome to mutate
//	              geneReplacementRate: new Option(0.01),  // completely replacing a genes properties
//
//	              reset: function() {
//	                this.minHiddenLayers.reset();
//	                this.maxHiddenLayers.reset();
//	                this.maxNeuronsPerLayer.reset();
//
//	                this.layerMutationRate.reset();
//	                this.geneMutationRate.reset();
//	                this.geneReplacementRate.reset();
//	              }
//	            },
//
//	            neuron: {
//	              minThreshold: new Option(0),
//	              maxThreshold: new Option(1.5), // 30
//	              maxRelaxation: new Option(99),
//
//	              thresholdMutationRate: new Option(0.1),
//	              relaxationMutationRate: new Option(0.1),
//
//	              axonGeneReplacementRate: new Option(0.01),
//
//	              reset: function() {
//	                this.minThreshold.reset();
//	                this.maxThreshold.reset();
//	                this.maxRelaxation.reset();
//
//	                this.thresholdMutationRate.reset();
//	                this.relaxationMutationRate.reset();
//
//	                this.axonGeneReplacementRate.reset();
//	              }
//	            },
//
//	            axon: {
//	              maxStrength: new Option(0.20), // 30
//
//	              minStrengthening: new Option(0.000001),
//	              maxStrengthening: new Option(0.00002),
//
//	              minWeakening: new Option(0.000001),
//	              maxWeakening: new Option(0.000005),
//
//	              strengthMutationRate: new Option(0.1),
//	              strengtheningMutationRate: new Option(0.05),
//	              weakeningMutationRate: new Option(0.05),
//
//	              reset: function() {
//	                this.maxStrength.reset();
//
//	                this.minStrengthening.reset();
//	                this.maxStrengthening.reset();
//	                this.minWeakening.reset();
//	                this.maxWeakening.reset();
//
//	                this.strengthMutationRate.reset();
//	                this.strengtheningMutationRate.reset();
//	                this.weakeningMutationRate.reset();
//	              }
//	            },
//
//	            reset: function() {
//	              this.evolution.reset();
//	              this.genome.reset();
//	              this.life.reset();
//	              this.sensor.reset();
//	              this.movement.reset();
//	              this.brain.reset();
//	              this.neuron.reset();
//	              this.axon.reset();
//	            }
//	          };
//
//	          return options;
//	        }
//	      );

}
